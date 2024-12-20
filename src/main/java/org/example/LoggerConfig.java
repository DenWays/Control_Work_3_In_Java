package org.example;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    public static Logger createLogger(Class<?> className) throws IOException {
        Handler fileHandler = new FileHandler("logger.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        Logger logger = Logger.getLogger(className.getName());
        logger.addHandler(fileHandler);
        return logger;
    }
}
