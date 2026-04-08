package service;

import model.Auction;

public class Report {

    public static void print(AuctionHouse house) {

        int total = house.getAuctions().size();
        int sold = 0;

        for (Auction a : house.getAuctions()) {
            if (a.isSold()) sold++;
        }

        System.out.println("Total auctions: " + total);
        System.out.println("Sold: " + sold);
        System.out.println("Commission: " + house.getTotalCommission());
    }
}