package com.skd.data_miner.logs;

import com.skd.data_miner.logs.issues.IssueRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Async, non-blocking log capture appender.
 *
 * <p>{@link #append(LogEvent)} only formats the line and hands it to a bounded
 * queue; a single daemon thread owns the file and drains the queue in batches
 * with one flush per batch. The logging thread never touches disk and never
 * blocks: if the queue is full the line is dropped and counted.
 */
public class CapturedAppender extends AbstractAppender {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    private static final int QUEUE_CAPACITY = 8192;
    private static final int DRAIN_BATCH = 1024;

    private final Path logFile;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    private final AtomicLong dropped = new AtomicLong();

    private volatile boolean stopped = false;
    private Thread writerThread;

    public CapturedAppender(Path logFile) {
        super("DataMinerCapture", null, null, true);
        this.logFile = logFile;
    }

    @Override
    public void start() {
        super.start();
        stopped = false;
        writerThread = new Thread(this::drainLoop, "DataMiner-LogCapture");
        writerThread.setDaemon(true);
        writerThread.start();
    }

    @Override
    public void append(LogEvent event) {
        Level level = event.getLevel();
        String loggerName = event.getLoggerName();
        if (loggerName != null && loggerName.startsWith("com.skd.data_miner")) {
            return;
        }

        boolean isLootrChat = "System".equals(loggerName) && level == Level.INFO;
        if (level != Level.WARN && level != Level.ERROR && !isLootrChat) {
            return;
        }

        String timestamp = TIME_FMT.format(Instant.ofEpochMilli(event.getTimeMillis()));
        String msg = event.getMessage().getFormattedMessage();
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(timestamp).append("][").append(level.name())
                .append("][").append(loggerName != null ? loggerName : "?").append("] ")
                .append(msg);

        String tag = IssueRegistry.check(loggerName, msg, Thread.currentThread().getName());
        if (!tag.isEmpty()) {
            sb.insert(0, tag);
        }

        Throwable thrown = event.getThrown();
        if (thrown != null) {
            sb.append(System.lineSeparator()).append(formatThrowable(thrown));
        }

        if (!queue.offer(sb.toString())) {
            dropped.incrementAndGet();
        }
    }

    private void drainLoop() {
        BufferedWriter writer = null;
        try {
            List<String> batch = new ArrayList<>(DRAIN_BATCH);
            while (true) {
                String first;
                try {
                    first = queue.poll(200, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    first = queue.poll();
                }

                if (first == null) {
                    if (stopped && queue.isEmpty()) {
                        break;
                    }
                    continue;
                }

                if (writer == null) {
                    writer = openWriter();
                    if (writer == null) {
                        // Could not open the file; drop this line rather than spinning on I/O.
                        continue;
                    }
                }

                batch.clear();
                batch.add(first);
                queue.drainTo(batch, DRAIN_BATCH - 1);

                try {
                    for (String line : batch) {
                        writer.write(line);
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("[DataMiner] Failed to write captured log: " + e.getMessage());
                }
            }
        } finally {
            if (writer != null) {
                try {
                    long d = dropped.get();
                    if (d > 0) {
                        writer.write("[DataMiner] " + d + " captured log line(s) dropped (queue full)");
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private BufferedWriter openWriter() {
        try {
            Files.createDirectories(logFile.getParent());
            return Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("[DataMiner] Failed to open captured log: " + e.getMessage());
            return null;
        }
    }

    private String formatThrowable(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append(System.lineSeparator()).append("\tat ").append(ste);
        }
        if (t.getCause() != null) {
            sb.append(System.lineSeparator()).append("Caused by: ");
            appendCause(sb, t.getCause(), 0);
        }
        return sb.toString();
    }

    private void appendCause(StringBuilder sb, Throwable t, int depth) {
        if (depth > 5) return;
        sb.append(t.getClass().getName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append(System.lineSeparator()).append("\tat ").append(ste);
        }
        if (t.getCause() != null) {
            sb.append(System.lineSeparator()).append("Caused by: ");
            appendCause(sb, t.getCause(), depth + 1);
        }
    }

    @Override
    public void stop() {
        stopped = true;
        Thread t = writerThread;
        if (t != null) {
            t.interrupt();
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        super.stop();
    }
}
