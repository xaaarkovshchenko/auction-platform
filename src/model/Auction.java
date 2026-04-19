package model;

import utils.ConsoleColors;
import java.util.List;
import java.util.ArrayList;

/**
 * Die Klasse Auction repräsentiert eine einzelne Auktion.
 *
 * Sie verwaltet:
 * - den aktuellen Preisverlauf
 * - die teilnehmenden Bieter
 * - den Gewinner
 * - den gesamten Ablauf der Auktion (Thread)
 *
 * Die Auktion läuft parallel als eigener Thread und interagiert
 * mit mehreren Bidder-Threads.
 */
public class Auction implements Runnable {

    private Item item;
    private double startPrice;
    private double minPrice;
    private double currentPrice;

    private volatile boolean active = true;
    private volatile boolean finished = false;

    private List<Double> priceHistory = new ArrayList<>();
    private boolean finalRound = false;

    private List<Bidder> bidders;
    private Bidder winner;
    private List<Thread> bidderThreads = new ArrayList<>();

    private static final Object PRINT_LOCK = new Object();
    private String color;

    /**
     * Konstruktor zur Erstellung einer Auktion
     *
     * @param item Artikel, der verkauft wird
     * @param startPrice Startpreis der Auktion
     * @param minPrice Mindestpreis der Auktion
     * @param bidders Liste der teilnehmenden Bieter
     * @param color Farbe für die Konsolenausgabe
     */
    public Auction(Item item, double startPrice, double minPrice, List<Bidder> bidders, String color) {
        this.item = item;
        this.startPrice = startPrice;
        this.minPrice = minPrice;
        this.currentPrice = startPrice;
        this.bidders = bidders;
        this.color = color;
    }

    /**
     * Erzeugt ein Präfix für Konsolenausgaben (inkl. Artikelname und Farbe)
     */
    private String prefix() {
        return color + "[" + item.getName() + "] " + ConsoleColors.RESET;
    }

    /**
     * Startet die Auktion als separaten Thread.
     *
     * Ablauf:
     * - Ausgabe des Headers
     * - Start aller Bidder-Threads
     * - Preis wird schrittweise reduziert
     * - finale Runde bei Mindestpreis
     * - Beenden der Auktion und Stoppen aller Threads
     */
    @Override
    public void run() {

        printAuctionHeader();

        // Start aller Bieter als eigene Threads
        for (Bidder b : bidders) {
            b.setAuction(this);
            Thread t = new Thread(b, b.getName());
            bidderThreads.add(t);
            t.start();
        }

        // Haupt-Auktionsschleife
        while (active) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }

            if (!active) break;

            double oldPrice = currentPrice;

            double progress = (startPrice - currentPrice) / (startPrice - minPrice);
            double reductionRate = 0.02 + (progress * 0.08);
            double randomFactor = 0.9 + (Math.random() * 0.2);

            double newPrice = currentPrice - (currentPrice * reductionRate * randomFactor);

            if (newPrice < minPrice) {
                newPrice = minPrice;
            }

            currentPrice = Math.round(newPrice * 100.0) / 100.0;

            priceHistory.add(currentPrice);

            System.out.println(prefix() + "📉 Preisupdate:");
            System.out.printf(prefix() + "➡️ %.2f€ → %.2f€\n", oldPrice, currentPrice);
            System.out.println(color + "═══════════════════════════════════" + ConsoleColors.RESET);

            // Finale Runde, wenn Mindestpreis erreicht wird
            if (currentPrice == minPrice && !finalRound) {

                finalRound = true;

                System.out.println(prefix() + "🔥 LETZTE RUNDE! Letzte Chance!");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    break;
                }

                continue;
            }

            // Ende der Auktion nach finaler Runde
            if (currentPrice == minPrice && finalRound) {
                break;
            }
        }

        // Auktion beenden
        active = false;
        finished = true;

        // Alle Bieter-Threads stoppen
        for (Thread t : bidderThreads) {
            t.interrupt();
        }

        // Auf Beendigung aller Threads warten
        for (Thread t : bidderThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gibt die Auktionsinformationen und die Bietertabelle aus
     */
    private void printAuctionHeader() {
        synchronized (PRINT_LOCK) {

            System.out.println(color + "\n🏆 =============================== 🏆" + ConsoleColors.RESET);

            System.out.println(color + "📦 AUKTION: " + item.getName() + ConsoleColors.RESET);
            System.out.println(color + "📂 Kategorie: " + item.getCategory() + ConsoleColors.RESET);
            System.out.printf(color + "💰 Startpreis: %.2f€\n" + ConsoleColors.RESET, startPrice);
            System.out.printf(color + "🔻 Mindestpreis: %.2f€\n" + ConsoleColors.RESET, minPrice);

            System.out.println(color + "\n👥 Bieter für: " + item.getName() + ConsoleColors.RESET);
            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);

            System.out.printf(color + "| %-12s | %-12s | %-10s |\n" + ConsoleColors.RESET,
                    "👤 Name", "🤖 Typ", "💰 Budget");

            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);

            for (Bidder b : bidders) {
                System.out.printf(color + "| %-12s | %-12s | %-10.2f€ |\n" + ConsoleColors.RESET,
                        b.getName(),
                        b.getType(),
                        b.getBudget());
            }

            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);
            System.out.println(color + "🏆 =============================== 🏆\n" + ConsoleColors.RESET);
        }
    }

    /**
     * Wird von einem Bidder aufgerufen, um ein Gebot abzugeben.
     * Setzt den Gewinner und beendet die Auktion.
     *
     * @param bidder der erfolgreiche Bieter
     */
    public synchronized void placeBid(Bidder bidder) {
        if (!active) return;

        winner = bidder;
        active = false;

        bidder.decreaseBudget(currentPrice);

        synchronized (PRINT_LOCK) {
            System.out.println(prefix() + "🔥 GEBOT ERFOLGREICH!");
            System.out.println(prefix() + "👤 " + bidder.getName() + " kauft den Artikel!");
            System.out.printf(prefix() + "💰 Preis: %.2f€\n\n", currentPrice);
        }
    }

    /**
     * Gibt zurück, ob die Auktion noch aktiv ist
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gibt zurück, ob die Auktion vollständig beendet ist
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Aktueller Preis der Auktion
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Startpreis der Auktion
     */
    public double getStartPrice() {
        return startPrice;
    }

    /**
     * Gibt zurück, ob ein Artikel verkauft wurde
     */
    public boolean isSold() {
        return winner != null;
    }

    /**
     * Endpreis der Auktion
     */
    public double getFinalPrice() {
        return currentPrice;
    }

    /**
     * Gibt das versteigerte Item zurück
     */
    public Item getItem() {
        return item;
    }

    /**
     * Mindestpreis der Auktion
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * Gewinner der Auktion
     */
    public Bidder getWinner() {
        return winner;
    }
}