package service;

import model.Auction;
import java.util.List;
import model.Bidder;


public class Report {

    public static void print(AuctionHouse house) {

        // 🔥 ИСТОРИЯ
        List<Auction> auctions = house.getHistory();

        System.out.println("\n📊 =============================== 📊");
        System.out.println("📊           REPORT               📊");
        System.out.println("📊 =============================== 📊\n");

        if (auctions.isEmpty()) {
            System.out.println("❌ Keine abgeschlossenen Auktionen vorhanden.\n");
            return;
        }

        int total = auctions.size();
        int sold = 0;
        double revenue = 0;

        Auction bestAuction = null;
        double highestPrice = 0;

        for (Auction a : auctions) {

            if (!a.isFinished()) continue;

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
        System.out.printf("➡️ Gesamtprovision: %.2f€\n", house.getTotalCommission());

        if (bestAuction != null) {
            System.out.printf("🏆 Beste Auktion: %s (%.2f€)\n",
                    bestAuction.getItem().getName(),
                    highestPrice);
        }

        // 🔥 TABELLE
        System.out.println("\n📋 Alle Auktionen (History):");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-10s | %-10s | %-12s | %-12s | %-12s |\n",
                "Item", "Start", "Final", "Status", "Winner", "Type");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Auction a : auctions) {

            String status;
            String winnerName = "-";
            String bidderType = "-";

            if (!a.isFinished()) {
                status = "⏳ RUNNING";
            } else if (a.isSold()) {
                status = "✅ SOLD";

                Bidder winner = a.getWinner();
                if (winner != null) {
                    winnerName = winner.getName();
                    bidderType = winner.getType().toString();
                }

            } else {
                status = "❌ NOT SOLD";
            }

            System.out.printf("| %-10s | %-10.2f | %-10.2f | %-12s | %-12s | %-12s |\n",
                    a.getItem().getName(),
                    a.getStartPrice(),
                    a.getFinalPrice(),
                    status,
                    winnerName,
                    bidderType);
        }

        System.out.println("------------------------------------------------------------------------------------------\n");

    }
}