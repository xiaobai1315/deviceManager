package com.study.Framework.utils;

public class LogUtil {

    public static String getClassName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getClassName();
    }

    public static void info(String message) {
//        Logger logger = Logger.getLogger(getClassName());
//        logger.info(message);
    }

    public static void info(String tag, String message) {
//        Logger logger = Logger.getLogger(getClassName());
//        logger.info("【" + tag + "】" + message);
    }

    public static void error(String message) {
//        Logger logger = Logger.getLogger(getClassName());
//        logger.error(message);
    }
}
