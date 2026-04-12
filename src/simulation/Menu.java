package simulation;

import model.*;
import service.*;

import java.util.*;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private AuctionHouse house = AuctionHouse.getInstance();

    public void start() {

        while (true) {

            clearScreen();

            System.out.println("=================================");
            System.out.println("|     🏠 AUCTION PLATFORM       |");
            System.out.println("=================================");
            System.out.println("📊 Auktionen: " + house.getAuctions().size());
            System.out.println("💰 Provision: " + house.getTotalCommission() + "€");
            System.out.println("---------------------------------\n");

            System.out.println("1️⃣  Neue Auktion erstellen");
            System.out.println("2️⃣  Simulation starten");
            System.out.println("3️⃣  Report anzeigen");
            System.out.println("4️⃣  Auktionen anzeigen");
            System.out.println("0️⃣  Exit");
            System.out.println("=================================");
            System.out.print("👉 Auswahl: ");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\n❌ Bitte nur Zahlen eingeben!\n");
                scanner.nextLine();
                waitForEnter();
                continue;
            }

            if (choice < 0 || choice > 4) {
                System.out.println("\n❌ Ungültige Auswahl!\n");
                waitForEnter();
                continue;
            }

            switch (choice) {
                case 1 -> createAuction();

                case 2 -> {
                    if (house.getAuctions().isEmpty()) {
                        System.out.println("❌ Keine Auktionen vorhanden!\n");
                        break;
                    }

                    System.out.print("⚠️ Simulation wirklich starten? (y/n): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("y")) {
                        house.startAll();
                    } else {
                        System.out.println("❌ Abgebrochen.");
                    }
                }

                case 3 -> Report.print(house);

                case 4 -> showAuctions();

                case 0 -> {
                    System.out.println("👋 Programm beendet. Danke fürs Nutzen!");
                    return;
                }
            }

            waitForEnter();
        }
    }

    private void createAuction() {

        try {
            String name = readName();
            Category category = readCategory();
            double start = readStartPrice();
            double min = readMinPrice(start);

            List<Bidder> bidders = createBidders();

            Item item = new Item(name, category);
            Auction auction = new Auction(item, start, min, bidders);

            house.addAuction(auction);

            System.out.println("\n✅ Auktion erfolgreich erstellt!");
            System.out.println("📦 " + item.getName() + " wurde hinzugefügt.");
            System.out.println("📊 Anzahl Bieter: " + bidders.size());

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("🚫 Zurück zum Menü\n");
        }
    }

    private void showAuctions() {

        List<Auction> auctions = house.getAuctions();

        if (auctions.isEmpty()) {
            System.out.println("❌ Keine Auktionen vorhanden!");
            return;
        }

        System.out.println("\n📋 Alle Auktionen:");
        System.out.println("-------------------------------------------------");

        for (Auction a : auctions) {
            String status = a.isSold() ? "✅ SOLD" : "⏳ AKTIV";

            System.out.println("📦 " + a.getItem().getName()
                    + " | 💰 Start: " + a.getStartPrice()
                    + " | Status: " + status);
        }

        System.out.println("-------------------------------------------------");
    }

    private String readName() {
        for (int i = 1; i <= 3; i++) {
            System.out.print("\n📦 Artikelname eingeben: ");
            String name = scanner.nextLine();

            if (!name.trim().isEmpty() && name.matches(".*[a-zA-ZäöüÄÖÜ].*")) {
                return name;
            }

            System.out.println("❌ Ungültiger Name! Versuch " + i + "/3");
        }

        throw new RuntimeException("❌ Zu viele Fehler beim Namen!");
    }

    private Category readCategory() {
        Category[] categories = Category.values();

        for (int i = 1; i <= 3; i++) {
            System.out.println("\n📂 Kategorie wählen:");

            for (int j = 0; j < categories.length; j++) {
                System.out.println("👉 " + j + " - " + categories[j]);
            }

            System.out.print("Auswahl: ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Bitte Zahl eingeben! Versuch " + i + "/3");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 0 && choice < categories.length) {
                return categories[choice];
            }

            System.out.println("❌ Ungültige Auswahl! Versuch " + i + "/3");
        }

        throw new RuntimeException("❌ Zu viele Fehler bei Kategorie!");
    }

    private double readStartPrice() {
        for (int i = 1; i <= 3; i++) {
            System.out.print("💰 Startpreis: ");

            if (!scanner.hasNextDouble()) {
                System.out.println("❌ Zahl eingeben! Versuch " + i + "/3");
                scanner.next();
                continue;
            }

            double value = scanner.nextDouble();
            scanner.nextLine();

            if (value > 0) {
                return value;
            }

            System.out.println("❌ Muss > 0 sein! Versuch " + i + "/3");
        }

        throw new RuntimeException("❌ Zu viele Fehler beim Startpreis!");
    }

    private double readMinPrice(double start) {
        for (int i = 1; i <= 3; i++) {
            System.out.print("🔻 Mindestpreis: ");

            if (!scanner.hasNextDouble()) {
                System.out.println("❌ Zahl eingeben! Versuch " + i + "/3");
                scanner.next();
                continue;
            }

            double value = scanner.nextDouble();
            scanner.nextLine();

            if (value > 0 && value < start) {
                return value;
            }

            System.out.println("❌ Muss > 0 und < Startpreis sein! Versuch " + i + "/3");
        }

        throw new RuntimeException("❌ Zu viele Fehler beim Mindestpreis!");
    }

    private List<Bidder> createBidders() {
        List<Bidder> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            list.add(new Bidder(
                    i,
                    "Player_" + (i + 1),
                    500 + random.nextInt(2000),
                    BidderType.values()[random.nextInt(3)]
            ));
        }

        System.out.println("\n👥 Bieter Übersicht:");
        System.out.println("-------------------------------------------------");
        System.out.printf("| %-10s | %-12s | %-10s |\n", "👤 Name", "🤖 Typ", "💰 Budget");
        System.out.println("-------------------------------------------------");

        for (Bidder b : list) {
            System.out.printf("| %-10s | %-12s | %-10.2f |\n",
                    b.getName(),
                    b.getType(),
                    b.getBudget());
        }

        System.out.println("-------------------------------------------------");

        return list;
    }

    // 🔥 HELPER METHODS

    private void waitForEnter() {
        System.out.println("\n⏳ Drücke Enter zum Fortfahren...");
        scanner.nextLine();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}