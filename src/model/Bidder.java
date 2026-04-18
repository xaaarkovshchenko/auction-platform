package model;

import java.util.Random;

public class Bidder extends User implements Runnable {

    private double budget;
    private BidderType type;
    private Auction auction;
    private Random random = new Random();

    private double lastSeenPrice = -1;
    private String lastAction = "";

    public Bidder(int id, String name, double budget, BidderType type) {
        super(id, name);
        this.budget = budget;
        this.type = type;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    private String prefix() {
        return "[" + auction.getItem().getName() + "] ";
    }

    @Override
    public void run() {

        while (!auction.isFinished()) {

            if (!auction.isActive()) return;

            try {
                Thread.sleep(600 + random.nextInt(600));
            } catch (InterruptedException e) {
                return;
            }

            if (!auction.isActive() || auction.isFinished()) return;

            double price = auction.getCurrentPrice();

            if (price == lastSeenPrice) continue;

            lastSeenPrice = price;

            System.out.printf("%s[CHECK] %s prüft Preis: %.2f€\n",
                    prefix(), name, price);

            //  нет бюджета
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

            if (willBid) {

                System.out.printf("%s[BID  ] %s bietet!\n",
                        prefix(), name);

                auction.placeBid(this);
                return;

            } else {

                if (!"WAIT".equals(lastAction)) {
                    lastAction = "WAIT";

                    System.out.printf("%s[WAIT ] %s wartet...\n",
                            prefix(), name);
                }
            }
        }
    }

    // =================  AI =================

    private boolean decide(double price) {

        return switch (type) {
            case AGGRESSIVE -> aggressiveStrategy(price);
            case CONSERVATIVE -> conservativeStrategy(price);
            case SNIPER -> sniperStrategy(price);
        };
    }

    //  AGGRESSIVE
    private boolean aggressiveStrategy(double price) {

        double threshold = auction.getStartPrice() * 0.85;

        if (price < threshold) {
            return random.nextDouble() < 0.8;
        }

        return random.nextDouble() < 0.3;
    }

    //  CONSERVATIVE
    private boolean conservativeStrategy(double price) {

        double threshold = auction.getStartPrice() * 0.6;

        if (price < threshold) {
            return random.nextDouble() < 0.7;
        }

        return false;
    }

    //  SNIPER
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