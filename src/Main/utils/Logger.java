package utils;

import java.time.LocalTime;

/**
 * Utility-Klasse für einfache Konsolen-Logs.
 *
 * Diese Klasse stellt statische Methoden zur Verfügung,
 * um Ereignisse mit Zeitstempel auszugeben.
 *
 * Log-Level:
 * - INFO: allgemeine Informationen
 * - EVENT: wichtige Ereignisse im Ablauf
 * - SUCCESS: erfolgreiche Aktionen
 */
public class Logger {

    /**
     * Gibt eine Info-Nachricht mit Zeitstempel aus.
     *
     * @param message die auszugebende Nachricht
     */
    public static void info(String message) {
        System.out.println("[" + LocalTime.now() + "] [INFO] " + message);
    }

    /**
     * Gibt eine Event-Nachricht mit Zeitstempel aus.
     *
     * @param message die auszugebende Nachricht
     */
    public static void event(String message) {
        System.out.println("[" + LocalTime.now() + "] [EVENT] " + message);
    }

    /**
     * Gibt eine Success-Nachricht mit Zeitstempel aus.
     *
     * @param message die auszugebende Nachricht
     */
    public static void success(String message) {
        System.out.println("[" + LocalTime.now() + "] [SUCCESS] " + message);
    }
}