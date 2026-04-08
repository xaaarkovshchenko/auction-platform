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
        for (Auction auction : auctions) {
            Thread t = new Thread(auction);
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (auction.isSold()) {
                totalCommission += auction.getFinalPrice() * 0.01;
            }
        }
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public double getTotalCommission() {
        return totalCommission;
    }
}