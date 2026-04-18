package model;

import utils.ConsoleColors;
import java.util.List;
import java.util.ArrayList;

public class Auction implements Runnable {

    private Item item;
    private double startPrice;
    private double minPrice;
    private double currentPrice;

    private volatile boolean active = true;
    private volatile boolean finished = false;

    private List<Double> priceHistory = new ArrayList<>();
    private boolean finalRound = false;

    private List<Bidder> bidders;
    private Bidder winner;
    private List<Thread> bidderThreads = new ArrayList<>();

    private static final Object PRINT_LOCK = new Object();
    private String color;

    public Auction(Item item, double startPrice, double minPrice, List<Bidder> bidders, String color) {
        this.item = item;
        this.startPrice = startPrice;
        this.minPrice = minPrice;
        this.currentPrice = startPrice;
        this.bidders = bidders;
        this.color = color;
    }

    private String prefix() {
        return color + "[" + item.getName() + "] " + ConsoleColors.RESET;
    }

    @Override
    public void run() {

        printAuctionHeader();

        // 🔥 START BIDDERS
        for (Bidder b : bidders) {
            b.setAuction(this);
            Thread t = new Thread(b, b.getName());
            bidderThreads.add(t);
            t.start();
        }

        while (active) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }

            if (!active) break;

            double oldPrice = currentPrice;

            double progress = (startPrice - currentPrice) / (startPrice - minPrice);
            double reductionRate = 0.02 + (progress * 0.08);
            double randomFactor = 0.9 + (Math.random() * 0.2);

            double newPrice = currentPrice - (currentPrice * reductionRate * randomFactor);

            if (newPrice < minPrice) {
                newPrice = minPrice;
            }

            currentPrice = Math.round(newPrice * 100.0) / 100.0;

            priceHistory.add(currentPrice);

            System.out.println(prefix() + "📉 Preisupdate:");
            System.out.printf(prefix() + "➡️ %.2f€ → %.2f€\n", oldPrice, currentPrice);
            System.out.println(color + "═══════════════════════════════════" + ConsoleColors.RESET);

            // FINAL ROUND
            if (currentPrice == minPrice && !finalRound) {

                finalRound = true;

                System.out.println(prefix() + "🔥 LETZTE RUNDE! Letzte Chance!");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    break;
                }

                continue;
            }

            if (currentPrice == minPrice && finalRound) {
                break;
            }
        }

        // 🔥 STOP AUCTION
        active = false;
        finished = true;

        // 🔥 INTERRUPT ALL BIDDERS
        for (Thread t : bidderThreads) {
            t.interrupt();
        }

        // 🔥 WAIT ALL
        for (Thread t : bidderThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printAuctionHeader() {
        synchronized (PRINT_LOCK) {

            System.out.println(color + "\n🏆 =============================== 🏆" + ConsoleColors.RESET);

            // 🔥 ГЛАВНЫЙ ЗАГОЛОВОК
            System.out.println(color + "📦 AUKTION: " + item.getName() + ConsoleColors.RESET);
            System.out.println(color + "📂 Kategorie: " + item.getCategory() + ConsoleColors.RESET);
            System.out.printf(color + "💰 Startpreis: %.2f€\n" + ConsoleColors.RESET, startPrice);
            System.out.printf(color + "🔻 Mindestpreis: %.2f€\n" + ConsoleColors.RESET, minPrice);

            // 🔥 ВАЖНО: убрали дубликат item.getName()

            System.out.println(color + "\n👥 Bieter für: " + item.getName() + ConsoleColors.RESET);
            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);

            System.out.printf(color + "| %-12s | %-12s | %-10s |\n" + ConsoleColors.RESET,
                    "👤 Name", "🤖 Typ", "💰 Budget");

            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);

            for (Bidder b : bidders) {
                System.out.printf(color + "| %-12s | %-12s | %-10.2f€ |\n" + ConsoleColors.RESET,
                        b.getName(),
                        b.getType(),
                        b.getBudget());
            }

            System.out.println(color + "-------------------------------------------------" + ConsoleColors.RESET);
            System.out.println(color + "🏆 =============================== 🏆\n" + ConsoleColors.RESET);
        }
    }

    public synchronized void placeBid(Bidder bidder) {
        if (!active) return;

        winner = bidder;
        active = false;

        bidder.decreaseBudget(currentPrice);

        synchronized (PRINT_LOCK) {
            System.out.println(prefix() + "🔥 GEBOT ERFOLGREICH!");
            System.out.println(prefix() + "👤 " + bidder.getName() + " kauft den Artikel!");
            System.out.printf(prefix() + "💰 Preis: %.2f€\n\n", currentPrice);
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFinished() {
        return finished;
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
    public double getMinPrice() {
        return minPrice;
    }
    public Bidder getWinner() {
        return winner;
    }
}