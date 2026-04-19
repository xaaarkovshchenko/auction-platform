package utils;

import java.util.Random;

/**
 * Utility-Klasse für Zufallswerte.
 *
 * Diese Klasse stellt zentrale Methoden bereit,
 * um zufällige Zahlen zu generieren.
 *
 * Vorteil:
 * - weniger duplizierter Code
 * - bessere Wartbarkeit
 */
public class RandomUtil {

    private static final Random random = new Random();

    /**
     * Gibt eine zufällige Ganzzahl zwischen min und max zurück.
     *
     * @param min untere Grenze (inklusive)
     * @param max obere Grenze (inklusive)
     * @return zufällige Zahl
     */
    public static int nextInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Gibt eine zufällige Dezimalzahl zwischen 0 und 1 zurück.
     *
     * @return double zwischen 0.0 und 1.0
     */
    public static double nextDouble() {
        return random.nextDouble();
    }

    /**
     * Gibt eine zufällige Dezimalzahl zwischen min und max zurück.
     *
     * @param min untere Grenze
     * @param max obere Grenze
     * @return zufälliger double-Wert
     */
    public static double nextDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}