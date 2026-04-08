package model;

import java.util.Random;

public class Bidder extends User implements Runnable {

    private double budget;
    private BidderType type;
    private Auction auction;
    private Random random = new Random();
    private Category preferredCategory;

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

        // 🔥 HIER einfügen
        System.out.println("[Bidder] " + name + " gestartet | Typ: " + type + " | Budget: " + budget);

        while (auction.isActive()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double price = auction.getCurrentPrice();

            System.out.println("[Bidder] " + name + " prüft Preis: " + price);

            if (price > budget) {
                System.out.println("[Bidder] " + name + " hat nicht genug Budget.");
                continue;
            }

            double probability = calculateProbability(price);

            if (Math.random() < probability) {
                System.out.println("[Bidder] " + name + " entscheidet sich zu kaufen!");

                auction.placeBid(this);
                break;
            } else {
                System.out.println("[Bidder] " + name + " wartet...");
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
}