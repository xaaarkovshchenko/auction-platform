package model;

/**
 * Enum zur Darstellung von Artikelkategorien.
 *
 * Jede Kategorie besitzt ein Label, das für die
 * Konsolenausgabe verwendet wird (inkl. Emoji).
 */
public enum Category {

    ELECTRONICS("📱 Electronics"),
    CARS("🚗 Cars"),
    BOOKS("📚 Books"),
    FASHION("👕 Fashion"),
    SPORT("⚽ Sport");

    private final String label;

    /**
     * Konstruktor für eine Kategorie
     *
     * @param label Anzeige-Text der Kategorie
     */
    Category(String label) {
        this.label = label;
    }

    /**
     * Gibt den Anzeige-Text der Kategorie zurück
     */
    @Override
    public String toString() {
        return label;
    }
}