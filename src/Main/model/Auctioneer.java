package model;

/**
 * Repräsentiert einen Auktionator.
 *
 * Diese Klasse erbt von User und kann später erweitert werden,
 * um z. B. Auktionen aktiv zu steuern oder zu moderieren.
 */
public class Auctioneer extends User {

    /**
     * Konstruktor für den Auktionator
     *
     * @param id   eindeutige ID
     * @param name Name des Auktionators
     */
    public Auctioneer(int id, String name) {
        super(id, name);
    }
}