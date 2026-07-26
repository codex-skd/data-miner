package com.skd.data_miner.logs;

import com.mojang.logging.LogUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.slf4j.Logger;

import java.nio.file.Path;

public class LogRedirector {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void init(Path baseDir) {
        Path logFile = baseDir.resolve("logs/captured.log");

        org.apache.logging.log4j.core.LoggerContext ctx =
                (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        CapturedAppender appender = new CapturedAppender(logFile);
        appender.start();
        config.addAppender(appender);
        config.getRootLogger().addAppender(appender, null, null);

        AbstractFilter suppressFilter = new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (event.getLevel().intLevel() >= Level.WARN.intLevel()
                        && event.getLoggerName() != null
                        && !event.getLoggerName().startsWith("com.skd.data_miner")) {
                    return Result.DENY;
                }
                return Result.NEUTRAL;
            }
        };

        for (AbstractAppender app : config.getAppenders().values().stream()
                .filter(a -> a instanceof AbstractAppender)
                .map(a -> (AbstractAppender) a)
                .toList()) {
            String fileName = null;
            if (app instanceof RollingFileAppender rfa) {
                fileName = rfa.getFileName();
            } else if (app instanceof FileAppender fa) {
                fileName = fa.getFileName();
            }
            if (fileName != null && fileName.replace('\\', '/').contains("logs/")) {
                app.addFilter(suppressFilter);
                LOGGER.info("Filtro añadido a '{}' ({})", app.getName(), fileName);
            }
        }

        ctx.updateLoggers();
        LOGGER.info("Redirección de logs iniciada → {}", logFile);
    }
}
