package service;

import model.Auction;

import java.util.ArrayList;
import java.util.List;

public class AuctionHouse {

    private static AuctionHouse instance;

    // АКТИВНЫЕ аукции (текущая симуляция)
    private List<Auction> activeAuctions = new ArrayList<>();

    // ИСТОРИЯ (для Report)
    private List<Auction> history = new ArrayList<>();

    private double totalCommission = 0;

    private AuctionHouse() {}

    public static AuctionHouse getInstance() {
        if (instance == null) {
            instance = new AuctionHouse();
        }
        return instance;
    }

    // ================= ADD =================

    public void addAuction(Auction auction) {
        activeAuctions.add(auction);
    }

    // ================= START =================

    public void startAll() {

        if (activeAuctions.isEmpty()) {
            System.out.println("❌ Keine Auktionen vorhanden!");
            return;
        }

        List<Thread> threads = new ArrayList<>();

        System.out.println("\n🚀 STARTE ALLE AUKTIONEN PARALLEL...\n");

        // ALLE STARTEN
        for (Auction auction : activeAuctions) {
            Thread t = new Thread(auction, auction.getItem().getName());
            threads.add(t);
            t.start();
        }

        // WARTEN BIS ALLE FERTIG
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 🔥 3. ERGEBNISSE AUSGEBEN
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

        //  ВАЖНО → ПЕРЕНОС В HISTORY
        history.addAll(activeAuctions);

        //  5. ОЧИСТКА ТЕКУЩИХ
        activeAuctions.clear();
    }

    // ================= GETTERS =================

    // для Menu (активные)
    public List<Auction> getAuctions() {
        return activeAuctions;
    }

    // для Report (ВСЕ)
    public List<Auction> getHistory() {
        return history;
    }

    public double getTotalCommission() {
        return totalCommission;
    }
}