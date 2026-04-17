package service;

import model.Auction;

import java.util.ArrayList;
import java.util.List;

public class AuctionHouse {

    private static AuctionHouse instance;
    private List<Auction> auctions = new ArrayList<>();
    private double totalCommission = 0;

    private AuctionHouse() {}

    public static AuctionHouse getInstance() {
        if (instance == null) {
            instance = new AuctionHouse();
        }
        return instance;
    }

    public void addAuction(Auction auction) {
        auctions.add(auction);
    }

    public void startAll() {

        List<Thread> threads = new ArrayList<>();

        System.out.println("\n🚀 STARTE ALLE AUKTIONEN PARALLEL...\n");

        // 🔥 1. ALLE STARTEN
        for (Auction auction : auctions) {
            Thread t = new Thread(auction, auction.getItem().getName());
            threads.add(t);
            t.start();
        }

        // 🔥 2. WARTEN BIS ALLE FERTIG
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 🔥 3. ERST JETZT → ALLE ERGEBNISSE
        System.out.println("\n\n🏁 ===== ALLE AUKTIONEN BEENDET =====");

        for (Auction auction : auctions) {

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
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public double getTotalCommission() {
        return totalCommission;
    }
}