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

        System.out.println("\n🏆 AUKTION STARTET 🏆\n");
        System.out.println("=================================");
        System.out.println("🚀 Bieter gestartet");
        System.out.println("👤 Name: " + name);
        System.out.println("🤖 Typ: " + type);
        System.out.println("💰 Budget: " + budget + "€");
        System.out.println("=================================");

        while (auction.isActive()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double price = auction.getCurrentPrice();

            System.out.println("\n🔎 " + name + " prüft aktuellen Preis: " + price + "€");

            if (price > budget) {
                System.out.println("❌ " + name + " hat nicht genug Budget!");
                continue;
            }

            double probability = calculateProbability(price);

            if (Math.random() < probability) {
                System.out.println("🔥 " + name + " gibt ein Gebot ab!");

                auction.placeBid(this);
                break;
            } else {
                System.out.println("⏳ " + name + " wartet...");
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