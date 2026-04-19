package utils;

/**
 * Utility-Klasse zur Definition von ANSI-Farbcodes
 * für die Konsolenausgabe.
 *
 * Diese Klasse enthält Konstanten für verschiedene Farben
 * und Textstile, die im Terminal verwendet werden können.
 *
 * Hinweis:
 * Die Darstellung funktioniert nur in Terminals,
 * die ANSI-Farbcodes unterstützen.
 */
public class ConsoleColors {

    /**
     * Setzt die Farbe zurück auf Standard.
     */
    public static final String RESET = "\u001B[0m";

    // ================= STANDARD FARBEN =================

    public static final String RED = "\u001B[31m";

    public static final String GREEN = "\u001B[32m";

    public static final String YELLOW = "\u001B[33m";

    public static final String BLUE = "\u001B[34m";

    public static final String PURPLE = "\u001B[35m";

    public static final String CYAN = "\u001B[36m";

    // ================= HELLE FARBEN =================

    public static final String BRIGHT_RED = "\u001B[91m";

    public static final String BRIGHT_GREEN = "\u001B[92m";

    public static final String BRIGHT_YELLOW = "\u001B[93m";

    public static final String BRIGHT_BLUE = "\u001B[94m";

    public static final String BRIGHT_PURPLE = "\u001B[95m";

    public static final String BRIGHT_CYAN = "\u001B[96m";

    // ================= STILE =================

    /** Fettgedruckter Text */
    public static final String BOLD = "\u001B[1m";

    /**
     * Privater Konstruktor verhindert die Instanziierung
     * dieser Utility-Klasse.
     */
    private ConsoleColors() {
    }

    /**
     * Alle verfügbaren Farben in einem Array.
     * Wird verwendet, um automatisch Farben zu verteilen.
     */
    public static final String[] ALL_COLORS = {
            RED,
            GREEN,
            YELLOW,
            BLUE,
            PURPLE,
            CYAN,

            BRIGHT_RED,
            BRIGHT_GREEN,
            BRIGHT_YELLOW,
            BRIGHT_BLUE,
            BRIGHT_PURPLE,
            BRIGHT_CYAN
    };
}