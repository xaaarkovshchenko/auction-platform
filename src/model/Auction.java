package model;

import utils.ConsoleColors;
import java.util.List;
import java.util.ArrayList;
import utils.Logger;

public class Auction implements Runnable {

    private Item item;
    private double startPrice;
    private double minPrice;
    private double currentPrice;
    private volatile boolean active = true;
    private List<Double> priceHistory = new ArrayList<>();
    private boolean finalRound = false;
    private List<Bidder> bidders;
    private Bidder winner;
    private List<Thread> bidderThreads = new ArrayList<>();

    public Auction(Item item, double startPrice, double minPrice, List<Bidder> bidders) {
        this.item = item;
        this.startPrice = startPrice;
        this.minPrice = minPrice;
        this.currentPrice = startPrice;
        this.bidders = bidders;
    }

    @Override
    public void run() {

        System.out.println("\n🏆 =============================== 🏆");
        Logger.info("Auktion gestartet: " + item.getName());
        System.out.println("📦 AUKTION STARTET");
        printBiddersTable();
        System.out.println(item);
        System.out.println("💰 Startpreis: " + startPrice + "€");
        System.out.println("🔻 Mindestpreis: " + minPrice + "€");
        System.out.println("🏆 =============================== 🏆\n");

        //  Threads starten
        for (Bidder b : bidders) {
            b.setAuction(this);
            Thread t = new Thread(b, b.getName());
            bidderThreads.add(t);
            t.start();
        }

        while (active) {

            try {
                Thread.sleep(1000);
                if (!active) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double oldPrice = currentPrice;

            //  Fortschritt berechnen
            double progress = (startPrice - currentPrice) / (startPrice - minPrice);

            //  dynamische Reduktion
            double reductionRate = 0.02 + (progress * 0.08);

            //  Zufall für Realismus
            double randomFactor = 0.9 + (Math.random() * 0.2);

            double newPrice = currentPrice - (currentPrice * reductionRate * randomFactor);

            //  nicht unter Mindestpreis
            if (newPrice < minPrice) {
                newPrice = minPrice;
            }

            //  runden
            currentPrice = Math.round(newPrice * 100.0) / 100.0;

            Logger.event("Preis geändert von " + oldPrice + "€ auf " + currentPrice + "€");
            priceHistory.add(currentPrice);

            System.out.println("📉 Preisupdate:");
            System.out.printf("➡️ %.2f€ → %.2f€\n", oldPrice, currentPrice);
            System.out.println("---------------------------------");

            //  FINAL ROUND
            if (currentPrice == minPrice && !finalRound) {

                finalRound = true;

                Logger.info("🔥 Letzte Runde gestartet (Mindestpreis erreicht)");
                System.out.println("🔥 LETZTE RUNDE! Bieter haben letzte Chance!");

                try {
                    for (int i = 3; i > 0; i--) {
                        System.out.println("⏳ " + i + "...");
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            //  nach final round wirklich beenden
            if (currentPrice == minPrice && finalRound) {
                break;
            }
        }

        active = false;

        //  Ergebnis
        if (winner == null) {
            Logger.info("Auktion beendet - kein Käufer gefunden");

            System.out.println("❌ Kein Käufer gefunden. Artikel nicht verkauft.");
            printRanking();

        } else {
            Logger.success("Auktion beendet - Gewinner: "
                    + winner.getName() + " | Preis: " + currentPrice + "€");

            System.out.println("\n🏆 =============================== 🏆");
            System.out.println(ConsoleColors.GREEN + "🎉 VERKAUFT!" + ConsoleColors.RESET);
            System.out.println("👤 Gewinner: " + winner.getName());
            System.out.println("💰 Preis: " + currentPrice + "€");
            System.out.println("🏆 =============================== 🏆\n");
        }

        printPriceHistory();

        //  auf Threads warten
        for (Thread t : bidderThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void placeBid(Bidder bidder) {
        if (!active) return;

        winner = bidder;
        active = false;

        bidder.decreaseBudget(currentPrice);

        Logger.success("Gebot abgegeben von " + bidder.getName()
                + " für " + currentPrice + "€");

        System.out.println("\n🔥 GEBOT ERFOLGREICH!");
        System.out.println("👤 " + bidder.getName() + " kauft den Artikel!");
        System.out.println("💰 Preis: " + currentPrice + "€\n");
    }

    public boolean isActive() {
        return active;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public boolean isSold() {
        return winner != null;
    }

    public double getFinalPrice() {
        return currentPrice;
    }

    public Item getItem() {
        return item;
    }

    public void printRanking() {
        System.out.println("\n🏆 Bieter Ranking:");

        bidders.stream()
                .sorted((a, b) -> Double.compare(b.getBudget(), a.getBudget()))
                .forEach(b -> System.out.println(
                        "👤 " + b.getName() + " | 💰 " + b.getBudget() + "€"
                ));
    }

    public void printPriceHistory() {
        System.out.println("\n📈 Preisverlauf:");

        for (double p : priceHistory) {
            System.out.println("➡️ " + p + "€");
        }
    }

    private void printBiddersTable() {
        System.out.println("\n👥 Bieter Übersicht:");
        System.out.println("-------------------------------------------------");
        System.out.printf("| %-10s | %-12s | %-10s |\n", "👤 Name", "🤖 Typ", "💰 Budget");
        System.out.println("-------------------------------------------------");

        for (Bidder b : bidders) {
            System.out.printf("| %-10s | %-12s | %-10.2f |\n",
                    b.getName(),
                    b.getType(),
                    b.getBudget());
        }

        System.out.println("-------------------------------------------------\n");
    }
}