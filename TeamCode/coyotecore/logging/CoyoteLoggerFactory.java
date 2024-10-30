package si.vegamind.coyotecore.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CoyoteLoggerFactory {
    private CoyoteLoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return switch (detectPlatform()) {
            case JVM -> LoggerFactory.getLogger(clazz);
        };
    }

    private static Platform detectPlatform() {
        return Platform.JVM;
    }

    private enum Platform {
        JVM
    }
}