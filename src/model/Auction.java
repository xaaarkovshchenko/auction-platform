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
        System.out.println("\n--- Auction started: " + item.getName() + " ---");

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

            System.out.println("[Auction] " + item.getName() +
                    " | Preis: " + oldPrice + " → " + currentPrice);
        }

        active = false;

        if (winner == null) {
            System.out.println("[Auction] Kein Käufer gefunden.");
        } else {
            System.out.println("[Auction] Verkauft an " + winner.getName());
        }
    }

    public synchronized void placeBid(Bidder bidder) {
        if (!active) return;

        winner = bidder;
        active = false;

        bidder.decreaseBudget(currentPrice);

        System.out.println("Winner: " + bidder.getName() + " for " + currentPrice);
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