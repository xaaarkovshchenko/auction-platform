package service;

import model.Auction;
import java.util.List;

public class Report {

    public static void print(AuctionHouse house) {

        List<Auction> auctions = house.getAuctions();

        System.out.println("\n📊 =============================== 📊");
        System.out.println("📊           REPORT               📊");
        System.out.println("📊 =============================== 📊\n");

        if (auctions.isEmpty()) {
            System.out.println("❌ Keine Auktionen vorhanden.\n");
            return;
        }

        int total = auctions.size();
        int sold = 0;
        double revenue = 0;

        Auction bestAuction = null;
        double highestPrice = 0;

        for (Auction a : auctions) {
            if (a.isSold()) {
                sold++;
                revenue += a.getFinalPrice();

                if (a.getFinalPrice() > highestPrice) {
                    highestPrice = a.getFinalPrice();
                    bestAuction = a;
                }
            }
        }

        double avg = sold > 0 ? revenue / sold : 0;

        // 🔥 ÜBERSICHT
        System.out.println("📈 Übersicht:");
        System.out.println("➡️ Gesamt Auktionen: " + total);
        System.out.println("➡️ Verkauft: " + sold);
        System.out.println("➡️ Nicht verkauft: " + (total - sold));
        System.out.printf("➡️ Umsatz: %.2f€\n", revenue);
        System.out.printf("➡️ Durchschnittspreis: %.2f€\n", avg);
        System.out.printf("➡️ Provision: %.2f€\n", house.getTotalCommission());

        if (bestAuction != null) {
            System.out.printf("🏆 Beste Auktion: %.2f€\n", highestPrice);
        }

        // 🔥 TABELLE
        System.out.println("\n📋 Auktionen:");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("| %-12s | %-10s | %-10s | %-10s |\n",
                "Item", "Start", "Final", "Status");
        System.out.println("------------------------------------------------------------------");

        for (Auction a : auctions) {
            String status = a.isSold() ? "✅ SOLD" : "❌ OPEN";

            System.out.printf("| %-12s | %-10.2f | %-10.2f | %-10s |\n",
                    a.getItem().getName(),
                    a.getStartPrice(),
                    a.getFinalPrice(),
                    status);
        }

        System.out.println("------------------------------------------------------------------\n");
    }
}