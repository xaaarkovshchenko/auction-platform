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

            if (!auction.isActive()) {
                return;
            }

            try {
                Thread.sleep(900);
            } catch (InterruptedException e) {
                //  sofort stoppen wenn Thread beendet wird
                return;
            }

            if (!auction.isActive() || auction.isFinished()) {
                return;
            }

            double price = auction.getCurrentPrice();

            // nur reagieren wenn Preis sich geändert hat
            if (price == lastSeenPrice) {
                continue;
            }

            lastSeenPrice = price;

            if (!auction.isActive()) return;

            System.out.printf("%s[CHECK] %s prüft Preis: %.2f€\n",
                    prefix(), name, price);

            //  nicht genug Budget
            if (price > budget) {

                if (!"NO_MONEY".equals(lastAction)) {
                    lastAction = "NO_MONEY";

                    System.out.printf("%s[STOP ] %s hat kein Budget!\n",
                            prefix(), name);
                }
                continue;
            }

            double probability = calculateProbability(price);

            if (!auction.isActive()) return;

            if (random.nextDouble() < probability) {

                if (!auction.isActive()) return;

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