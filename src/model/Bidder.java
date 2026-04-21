package model;

import java.util.Random;

/**
 * Die Klasse Bidder repräsentiert einen Bieter in der Auktion.
 *
 * Jeder Bidder läuft als eigener Thread und trifft Entscheidungen,
 * ob er ein Gebot abgibt oder wartet.
 *
 * Das Verhalten wird durch den BidderType (Strategie) bestimmt.
 */
public class Bidder extends User implements Runnable {

    private double budget;
    private BidderType type;
    private Auction auction;
    private Random random = new Random();

    private double lastSeenPrice = -1;  //merkt sich letzten Preis
    private String lastAction = "";    //vermeidet Spam in Konsole

    /**
     * Konstruktor für einen Bieter
     *
     * @param id     eindeutige ID
     * @param name   Name des Bieters
     * @param budget verfügbares Budget
     * @param type   Strategie des Bieters
     */
    public Bidder(int id, String name, double budget, BidderType type) {
        super(id, name);
        this.budget = budget;
        this.type = type;
    }

    /**
     * Setzt die Auktion, an der der Bieter teilnimmt
     *
     * @param auction aktuelle Auktion
     */
    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    /**
     * Erstellt ein Präfix für die Konsolenausgabe
     */
    private String prefix() {
        return "[" + auction.getItem().getName() + "] ";
    }

    /**
     * Hauptlogik des Bieters (Thread)
     *
     * Ablauf:
     * - überprüft regelmäßig den aktuellen Preis
     * - entscheidet basierend auf Strategie
     * - gibt ggf. ein Gebot ab
     */
    @Override
    public void run() {

        while (!auction.isFinished()) {

            if (!auction.isActive()) return;

            try {
                Thread.sleep(600 + random.nextInt(600)); // wartet zwischen 600ms – 1200ms
            } catch (InterruptedException e) {
                return;
            }

            if (!auction.isActive() || auction.isFinished()) return;

            double price = auction.getCurrentPrice();

            // Nur reagieren, wenn sich der Preis geändert hat
            if (price == lastSeenPrice) continue;   //verhindert:Spam + unnötige Entscheidungen

            lastSeenPrice = price;

            System.out.printf("%s[CHECK] %s prüft Preis: %.2f€\n",
                    prefix(), name, price);

            // Prüfen, ob Budget ausreicht
            if (price > budget) {

                if (!"NO_MONEY".equals(lastAction)) {
                    lastAction = "NO_MONEY";

                    System.out.printf("%s[STOP ] %s hat kein Budget!\n",
                            prefix(), name);
                }
                continue;
            }

            boolean willBid = decide(price);

            if (!auction.isActive()) return;

            // Gebot abgeben
            if (willBid) {

                System.out.printf("%s[BID  ] %s bietet!\n",
                        prefix(), name);

                auction.placeBid(this);
                return;

            } else {

                // Warten
                if (!"WAIT".equals(lastAction)) {
                    lastAction = "WAIT";

                    System.out.printf("%s[WAIT ] %s wartet...\n",
                            prefix(), name);
                }
            }
        }
    }

    // ================= AI =================

    /**
     * Entscheidet basierend auf dem Bietertyp, ob geboten wird
     *
     * @param price aktueller Preis
     * @return true, wenn Gebot abgegeben wird
     */
    private boolean decide(double price) {

        return switch (type) {
            case AGGRESSIVE -> aggressiveStrategy(price);
            case CONSERVATIVE -> conservativeStrategy(price);
            case SNIPER -> sniperStrategy(price);
        };
    }

    /**
     * Aggressive Strategie:
     * Bietet früh und häufig
     */
    private boolean aggressiveStrategy(double price) {

        double threshold = auction.getStartPrice() * 0.85;

        if (price < threshold) {
            return random.nextDouble() < 0.8;
        }

        return random.nextDouble() < 0.3;
    }

    /**
     * Konservative Strategie:
     * Wartet auf niedrige Preise
     */
    private boolean conservativeStrategy(double price) {

        double threshold = auction.getStartPrice() * 0.6;

        if (price < threshold) {
            return random.nextDouble() < 0.7;
        }

        return false;
    }

    /**
     * Sniper Strategie:
     * Wartet bis zum Ende und bietet dann schnell
     */
    private boolean sniperStrategy(double price) {

        double progress =
                (auction.getStartPrice() - price) /
                        (auction.getStartPrice() - auction.getMinPrice());

        if (progress < 0.8) {
            return false;
        }

        return random.nextDouble() < 0.9;
    }

    // ================= UTILS =================

    /**
     * Reduziert das Budget nach einem erfolgreichen Kauf
     *
     * @param amount Betrag, der abgezogen wird
     */
    public void decreaseBudget(double amount) {
        budget -= amount;
    }

    /**
     * Gibt das aktuelle Budget zurück
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Gibt den Typ (Strategie) des Bieters zurück
     */
    public BidderType getType() {
        return type;
    }
}