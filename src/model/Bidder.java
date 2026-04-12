package model;

import java.util.Random;

public class Bidder extends User implements Runnable {

    private double budget;
    private BidderType type;
    private Auction auction;
    private Random random = new Random();
    private Category preferredCategory;
    private static final Object PRINT_LOCK = new Object();
    private double lastSeenPrice = -1;

    public Bidder(int id, String name, double budget, BidderType type) {
        super(id, name);
        this.budget = budget;
        this.type = type;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @Override
    public void run() {

        synchronized (PRINT_LOCK) {

        }

        while (true) {

            // 👉 sofort beenden
            if (!auction.isActive()) {
                return;
            }

            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 👉 nochmal check nach sleep
            if (!auction.isActive()) {
                return;
            }

            double price = auction.getCurrentPrice();

            //  nur reagieren wenn neuer Preis
            if (price == lastSeenPrice) {
                continue;
            }

            lastSeenPrice = price;

            if (!auction.isActive()) return;
            synchronized (PRINT_LOCK) {
                System.out.println("\n🔎 " + name + " prüft aktuellen Preis: " + price + "€");
            }

            if (price > budget) {
                if (!auction.isActive()) return;

                synchronized (PRINT_LOCK) {
                    System.out.println("❌ " + name + " hat nicht genug Budget!");
                }
                continue;
            }

            double probability = calculateProbability(price);

            if (!auction.isActive()) return;

            if (random.nextDouble() < probability) {

                if (!auction.isActive()) return;

                synchronized (PRINT_LOCK) {
                    System.out.println("🔥 " + name + " gibt ein Gebot ab!");
                }

                auction.placeBid(this);
                return;

            } else {
                if (!auction.isActive()) return;

                synchronized (PRINT_LOCK) {
                    System.out.println("⏳ " + name + " wartet...");
                }
            }
        }
    }

    private double calculateProbability(double price) {
        double base = 0.3;

        switch (type) {
            case AGGRESSIVE:
                base = 0.7;
                break;
            case CONSERVATIVE:
                base = 0.3;
                break;
            case SNIPER:
                base = 0.1;
                break;
        }

        return base * (1 - price / auction.getStartPrice());
    }

    public void decreaseBudget(double amount) {
        budget -= amount;
    }

    public double getBudget() {
        return budget;
    }

    public BidderType getType() {
        return type;
    }
}