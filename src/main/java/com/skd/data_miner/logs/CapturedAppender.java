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

public class CapturedAppender extends AbstractAppender {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    private final Path logFile;
    private BufferedWriter writer;

    public CapturedAppender(Path logFile) {
        super("DataMinerCapture", null, null, false);
        this.logFile = logFile;
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
        sb.append('[').append(timestamp).append("][").append(event.getLevel().name())
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

        writeLine(sb.toString());
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

    private void writeLine(String line) {
        try {
            if (writer == null) {
                Files.createDirectories(logFile.getParent());
                writer = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            synchronized (this) {
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("[DataMiner] Failed to write captured log: " + e.getMessage());
        }
    }

    @Override
    public synchronized void stop() {
        super.stop();
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {}
        }
    }
}
