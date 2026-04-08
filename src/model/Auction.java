package model;

import java.util.List;

public class Auction implements Runnable {

    private Item item;
    private double startPrice;
    private double minPrice;
    private double currentPrice;
    private boolean active = true;

    private List<Bidder> bidders;
    private Bidder winner;

    public Auction(Item item, double startPrice, double minPrice, List<Bidder> bidders) {
        this.item = item;
        this.startPrice = startPrice;
        this.minPrice = minPrice;
        this.currentPrice = startPrice;
        this.bidders = bidders;
    }

    @Override
    public void run() {
        System.out.println("\n🏆 =============================== 🏆");
        System.out.println("📦 AUKTION STARTET");
        System.out.println(item);
        System.out.println("💰 Startpreis: " + startPrice + "€");
        System.out.println("🔻 Mindestpreis: " + minPrice + "€");
        System.out.println("🏆 =============================== 🏆\n");

        for (Bidder b : bidders) {
            b.setAuction(this);
            new Thread(b).start();
        }

        while (active && currentPrice > minPrice) {
            try {
                Thread.sleep(1000); // langsamer!
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double oldPrice = currentPrice;

            // dynamische Reduktion
            currentPrice -= currentPrice * 0.05;

            if (currentPrice <= minPrice) {
                System.out.println("⚠️ Mindestpreis erreicht!");
            }

            System.out.println("📉 Preisupdate:");
            System.out.println("➡️ " + oldPrice + "€ → " + currentPrice + "€");
            System.out.println("---------------------------------");
        }

        active = false;

        if (winner == null) {
            System.out.println("❌ Kein Käufer gefunden. Artikel nicht verkauft.");
        } else {
            System.out.println("\n🏆 =============================== 🏆");
            System.out.println("🎉 VERKAUFT!");
            System.out.println("👤 Gewinner: " + winner.getName());
            System.out.println("💰 Preis: " + currentPrice + "€");
            System.out.println("🏆 =============================== 🏆\n");
        }
    }

    public synchronized void placeBid(Bidder bidder) {
        if (!active) return;

        winner = bidder;
        active = false;

        bidder.decreaseBudget(currentPrice);

        System.out.println("\n🔥 GEBOT ERFOLGREICH!");
        System.out.println("👤 " + bidder.getName() + " kauft den Artikel!");
        System.out.println("💰 Preis: " + currentPrice + "€\n");
    }

    public boolean isActive() {
        return active;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public boolean isSold() {
        return winner != null;
    }

    public double getFinalPrice() {
        return currentPrice;
    }
}