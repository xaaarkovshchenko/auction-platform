package model;

/**
 * Abstrakte Basisklasse für alle Benutzer im System.
 *
 * Von dieser Klasse erben:
 * - Bidder
 * - Auctioneer
 *
 * Sie enthält gemeinsame Eigenschaften wie ID und Name.
 */
public abstract class User {

    protected int id;
    protected String name;

    /**
     * Konstruktor für einen Benutzer
     *
     * @param id   eindeutige ID des Benutzers
     * @param name Name des Benutzers
     */
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gibt den Namen des Benutzers zurück
     *
     * @return Name
     */
    public String getName() {
        return name;
    }
}