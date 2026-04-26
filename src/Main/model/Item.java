package model;

/**
 * Repräsentiert einen Artikel, der in einer Auktion verkauft wird.
 *
 * Ein Item besitzt:
 * - einen Namen
 * - eine Kategorie
 */
public class Item {

    private String name;
    private Category category;

    /**
     * Konstruktor zur Erstellung eines Artikels
     *
     * @param name     Name des Artikels
     * @param category Kategorie des Artikels
     */
    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    /**
     * Gibt die Kategorie des Artikels zurück
     *
     * @return Kategorie
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Gibt den Namen des Artikels zurück
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt eine formatierte Darstellung des Artikels zurück
     */
    @Override
    public String toString() {
        return "📦 Item: " + name + " | Kategorie: " + category;
    }
}