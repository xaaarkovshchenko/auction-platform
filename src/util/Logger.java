package utils;

import java.time.LocalTime;

public class Logger {

    public static void info(String message) {
        System.out.println("[" + LocalTime.now() + "] [INFO] " + message);
    }

    public static void event(String message) {
        System.out.println("[" + LocalTime.now() + "] [EVENT] " + message);
    }

    public static void success(String message) {
        System.out.println("[" + LocalTime.now() + "] [SUCCESS] " + message);
    }
}