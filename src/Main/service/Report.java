package service;

import model.Auction;
import java.util.List;
import model.Bidder;

/**
 * Die Klasse Report erstellt eine Zusammenfassung aller abgeschlossenen Auktionen.
 *
 * Funktionen:
 * - Analyse der Auktionshistorie
 * - Berechnung von Kennzahlen (Umsatz, Durchschnitt, Provision)
 * - Anzeige der besten Auktion
 * - Ausgabe einer Tabelle mit allen Ergebnissen
 */
public class Report {

    /**
     * Gibt den vollständigen Report der Auktionen aus
     *
     * @param house zentrale Instanz des AuctionHouse
     */
    public static void print(AuctionHouse house) {

        // Historie aller Auktionen
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

        // Analyse aller Auktionen
        for (Auction a : auctions) {

            // Nur abgeschlossene berücksichtigen
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

        // Übersicht
        System.out.println("📈 Übersicht:");
        System.out.println("➡️ Gesamt Auktionen: " + total);
        System.out.println("➡️ Verkauft: " + sold);
        System.out.println("➡️ Nicht verkauft: " + (total - sold));
        System.out.printf("➡️ Umsatz: %.2f€\n", revenue);
        System.out.printf("➡️ Durchschnittspreis: %.2f€\n", avg);
        System.out.printf("➡️ Gesamtprovision: %.2f€\n", house.getTotalCommission());

        // Beste Auktion
        if (bestAuction != null) {
            System.out.printf("🏆 Beste Auktion: %s (%.2f€)\n",
                    bestAuction.getItem().getName(),
                    highestPrice);
        }

        // Tabelle
        System.out.println("\n📋 Alle Auktionen (History):");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-10s | %-10s | %-12s | %-12s | %-12s |\n",
                "Item", "Start", "Final", "Status", "Winner", "Type");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Auction a : auctions) {

            String status;
            String winnerName = "-";
            String bidderType = "-";

            // Status bestimmen
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

            // Tabellenzeile ausgeben
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