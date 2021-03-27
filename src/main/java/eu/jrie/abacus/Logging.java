package eu.jrie.abacus;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import static java.util.logging.Logger.getLogger;

abstract class Logging {

    private Logging() {}

    private static class LogFormatter extends SimpleFormatter {

        private static final String LOG_FORMAT = "[%1$tF %1$tT] [%2$-4s] [%3$-1s] %4$s %n";

        @Override
        public synchronized String format(LogRecord lr) {
            return String.format(
                    LOG_FORMAT,
                    new Date(lr.getMillis()),
                    lr.getLevel().getLocalizedName(),
                    lr.getLoggerName(),
                    lr.getMessage()
            );
        }
    }

    private static class LogHandler extends ConsoleHandler {
        LogHandler() {
            super();
            setFormatter(new LogFormatter());
            setOutputStream(System.out);
        }
    }

    static void configureLogging() {
        var root = getLogger("");
        for (var handler : root.getHandlers()) {
            root.removeHandler(handler);
        }
        root.addHandler(new LogHandler());
    }
}
