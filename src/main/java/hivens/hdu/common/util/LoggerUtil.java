package hivens.hdu.common.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtil {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void logDebug(String message) {
        log("DEBUG", message);
    }

    public static void logInfo(String message) {
        log("INFO", message);
    }

    public static void logWarn(String message) {
        log("WARN", message);
    }

    public static void logError(String message) {
        log("ERROR", message);
    }

    private static void log(String level, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LOGGER.info("[{}] [{}] {}", timestamp, level, message);
    }
}
