package service;

import model.Auction;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse AuctionHouse verwaltet alle Auktionen im System.
 *
 * Funktionen:
 * - Speichert aktive Auktionen (laufende Simulation)
 * - Speichert abgeschlossene Auktionen (History für Report)
 * - Startet alle Auktionen parallel (Multithreading)
 * - Berechnet die Gesamtprovision
 *
 * Implementiert als Singleton, damit es nur eine Instanz gibt.
 */
public class AuctionHouse {

    private static AuctionHouse instance;

    // Aktive Auktionen (nur aktuelle Simulation)
    private List<Auction> activeAuctions = new ArrayList<>();

    // Historie aller abgeschlossenen Auktionen (für Report)
    private List<Auction> history = new ArrayList<>();

    private double totalCommission = 0;

    /**
     * Privater Konstruktor (Singleton Pattern)
     */
    private AuctionHouse() {}

    /**
     * Gibt die einzige Instanz von AuctionHouse zurück
     *
     * @return AuctionHouse Instanz
     */
    public static AuctionHouse getInstance() {
        if (instance == null) {
            instance = new AuctionHouse();
        }
        return instance;
    }

    // ================= ADD =================

    /**
     * Fügt eine neue Auktion zur aktiven Liste hinzu
     *
     * @param auction die hinzuzufügende Auktion
     */
    public void addAuction(Auction auction) {
        activeAuctions.add(auction);
    }

    // ================= START =================

    /**
     * Startet alle aktiven Auktionen parallel.
     *
     * Ablauf:
     * - Start aller Auktionen als Threads
     * - Warten bis alle Threads beendet sind
     * - Ausgabe der Ergebnisse
     * - Berechnung der Provision
     * - Verschieben in die History
     * - Leeren der aktiven Liste
     */
    public void startAll() {

        if (activeAuctions.isEmpty()) {
            System.out.println("❌ Keine Auktionen vorhanden!");
            return;
        }

        List<Thread> threads = new ArrayList<>();

        System.out.println("\n🚀 STARTE ALLE AUKTIONEN PARALLEL...\n");

        // Start aller Auktionen als Threads
        for (Auction auction : activeAuctions) {
            Thread t = new Thread(auction, auction.getItem().getName());
            threads.add(t);
            t.start();
        }

        // Warten bis alle Auktionen beendet sind
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Ausgabe der Ergebnisse
        System.out.println("\n\n🏁 ===== ALLE AUKTIONEN BEENDET =====");

        for (Auction auction : activeAuctions) {

            System.out.println("\n📊 ===== AUKTION ABGESCHLOSSEN =====");
            System.out.println("📦 Artikel: " + auction.getItem().getName());

            if (auction.isSold()) {

                double price = auction.getFinalPrice();
                double commission = price * 0.01;

                totalCommission += commission;

                System.out.printf("💰 Verkaufspreis: %.2f€\n", price);
                System.out.printf("🏦 Provision (1%%): %.2f€\n", commission);

            } else {
                System.out.println("❌ Nicht verkauft");
            }

            System.out.println("====================================");
        }

        System.out.println("\n💰 Gesamtprovision: " + String.format("%.2f", totalCommission) + "€");
        System.out.println("✅ Alle Auktionen sind beendet!\n");

        // Verschieben in History
        history.addAll(activeAuctions);

        // Leeren der aktiven Auktionen
        activeAuctions.clear();
    }

    // ================= GETTERS =================

    /**
     * Gibt die aktuell aktiven Auktionen zurück
     *
     * @return Liste aktiver Auktionen
     */
    public List<Auction> getAuctions() {
        return activeAuctions;
    }

    /**
     * Gibt die Historie aller Auktionen zurück
     *
     * @return Liste abgeschlossener Auktionen
     */
    public List<Auction> getHistory() {
        return history;
    }

    /**
     * Gibt die gesamte Provision zurück
     *
     * @return Gesamtprovision
     */
    public double getTotalCommission() {
        return totalCommission;
    }
}