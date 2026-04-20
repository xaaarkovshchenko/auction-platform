package simulation;

import model.*;
import service.*;
import utils.ConsoleColors;
import java.util.*;

/**
 * Die Klasse Menu stellt die Benutzeroberfläche der Anwendung dar.
 *
 * Funktionen:
 * - Anzeige des Hauptmenüs
 * - Steuerung der Benutzerinteraktion
 * - Erstellung von Auktionen
 * - Start der Simulation
 * - Anzeige des Reports
 *
 * Diese Klasse ist der Einstiegspunkt für die Benutzersteuerung.
 */
public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private AuctionHouse house = AuctionHouse.getInstance();

    /**
     * Startet das Menü und läuft in einer Endlosschleife,
     * bis der Benutzer das Programm beendet.
     */
    public void start() {

        while (true) {
            pushUp();
            clearScreen();
            printHeader();

            int choice = readChoice();

            if (choice == -1) {
                waitForEnter();
                continue;
            }

            switch (choice) {

                case 1 -> createAuction();
                case 2 -> startSimulation();
                case 3 -> Report.print(house);
                case 4 -> createTestAuctions();

                case 0 -> {
                    System.out.println("👋 Programm beendet. Danke fürs Nutzen!");
                    return;
                }
            }

            waitForEnter();
        }
    }

    // ================= HEADER =================

    private void printHeader() {

        System.out.println("=================================");
        System.out.println("|     🏠 AUCTION PLATFORM       |");
        System.out.println("=================================");
        System.out.println("📊 Active Auktionen: " + house.getAuctions().size());
        System.out.printf("💰 Provision: %.2f€\n", house.getTotalCommission());
        System.out.println("---------------------------------\n");

        System.out.println("1️⃣  Neue Auktion erstellen");
        System.out.println("2️⃣  Simulation starten");
        System.out.println("3️⃣  Report anzeigen");
        System.out.println("4️⃣  Test: 5 Auktionen erstellen");
        System.out.println("0️⃣  Exit");
        System.out.println("=================================");
        System.out.print("👉 Auswahl: ");
    }

    private int readChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 0 || choice > 4) {
                System.out.println("❌ Ungültige Auswahl!");
                return -1;
            }

            return choice;

        } catch (InputMismatchException e) {
            System.out.println("❌ Bitte nur Zahlen eingeben!");
            scanner.nextLine();
            return -1;
        }
    }

    // ================= ACTIONS =================

    private void startSimulation() {

        if (house.getAuctions().isEmpty()) {
            System.out.println("❌ Keine Auktionen vorhanden!");
            return;
        }

        System.out.print("⚠️ Simulation wirklich starten? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            house.startAll();
        } else {
            System.out.println("❌ Abgebrochen.");
        }
    }

    private void createAuction() {

        try {
            String name = readName();
            Category category = readCategory();
            double start = readStartPrice();
            double min = readMinPrice(start);

            // 🔥 FIX: startPrice übergeben
            List<Bidder> bidders = createBidders(name, start);

            Item item = new Item(name, category);

            int index = house.getAuctions().size();

            Auction auction = new Auction(
                    item,
                    start,
                    min,
                    bidders,
                    getColor(index)
            );

            house.addAuction(auction);

            System.out.println("\n✅ Auktion erfolgreich erstellt!");
            System.out.println("📦 " + item.getName());
            System.out.println("📊 Bieter: " + bidders.size());

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("🚫 Zurück zum Menü\n");
        }
    }

    private void createTestAuctions() {

        Random random = new Random();

        for (int i = 0; i < 5; i++) {

            String itemName = "Item_" + (i + 1);

            double start = 1000 + random.nextInt(2000);

            // 🔥 FIX: start übergeben
            List<Bidder> bidders = createBidders(itemName, start);

            Item item = new Item(
                    itemName,
                    Category.values()[i % Category.values().length]
            );

            Auction auction = new Auction(
                    item,
                    start,
                    500 + random.nextInt(1000),
                    bidders,
                    getColor(i)
            );

            house.addAuction(auction);
        }

        System.out.println("\n🔥 5 Test-Auktionen erstellt!");
    }

    // ================= INPUT =================

    /**
     * Liest den Namen eines Artikels mit max. 3 Versuchen.
     * Bedingungen:
     * - mindestens 2 Zeichen
     * - darf nicht leer sein
     */
    private String readName() {

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.print("\n📦 Artikelname: ");
            String name = scanner.nextLine().trim();

            if (name.length() >= 2) {
                return name;
            }

            System.out.println("❌ Name muss mindestens 2 Zeichen haben! Versuch " + attempt + "/3");
        }

        throw new RuntimeException("🚫 Zu viele falsche Eingaben!");
    }

    /**
     * Kategorie mit max. 3 Versuchen
     */
    private Category readCategory() {

        Category[] categories = Category.values();

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.println("\n📂 Kategorie wählen:");

            for (int i = 0; i < categories.length; i++) {
                System.out.println(i + " -> " + categories[i]);
            }

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 0 && choice < categories.length) {
                    return categories[choice];
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
            }

            System.out.println("❌ Ungültige Eingabe! Versuch " + attempt + "/3");
        }

        throw new RuntimeException("🚫 Zu viele falsche Eingaben!");
    }

    /**
     * Startpreis mit max. 3 Versuchen
     * Liest den Startpreis mit max. 3 Versuchen.
     * Bedingungen:
     * - muss größer als 2 sein
     * - darf nicht negativ sein
     */
    private double readStartPrice() {

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.print("💰 Startpreis: ");

            try {
                double value = scanner.nextDouble();
                scanner.nextLine();

                if (value > 2) {
                    return value;
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
            }

            System.out.println("❌ Startpreis muss größer als 2€ sein! Versuch " + attempt + "/3");
        }

        throw new RuntimeException("🚫 Zu viele falsche Eingaben!");
    }

    /**
     * Liest den Mindestpreis mit max. 3 Versuchen.
     * Bedingungen:
     * - > 0
     * - mindestens 1€ kleiner als Startpreis
     */
    private double readMinPrice(double start) {

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.print("🔻 Mindestpreis: ");

            try {
                double value = scanner.nextDouble();
                scanner.nextLine();

                if (value > 0 && value <= start - 1) {
                    return value;
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
            }

            System.out.println("❌ Mindestpreis muss mindestens 1€ kleiner als Startpreis sein und > 0! Versuch " + attempt + "/3");
        }

        throw new RuntimeException("🚫 Zu viele falsche Eingaben!");
    }

    // ================= BIDDERS =================

    private List<Bidder> createBidders(String itemName, double startPrice) {

        List<Bidder> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {

            double budget;

            switch (i) {
                case 0 -> budget = startPrice * (1.2 + random.nextDouble() * 0.5);
                case 1 -> budget = startPrice * (1.0 + random.nextDouble() * 0.3);
                case 2 -> budget = startPrice * (0.8 + random.nextDouble() * 0.2);
                case 3 -> budget = startPrice * (0.6 + random.nextDouble() * 0.2);
                default -> budget = startPrice * (0.4 + random.nextDouble() * 0.2);
            }

            list.add(new Bidder(
                    i,
                    "Player_" + (i + 1),
                    Math.round(budget),
                    BidderType.values()[random.nextInt(3)]
            ));
        }

        printBiddersTable(list, itemName);

        return list;
    }

    private void printBiddersTable(List<Bidder> list, String itemName) {

        System.out.println("\n📦 " + itemName + " | 👥 Bieter Übersicht:");
        System.out.println("-------------------------------------------------");

        System.out.printf("| %-12s | %-12s | %-10s |\n",
                "👤 Name", "🤖 Typ", "💰 Budget");

        System.out.println("-------------------------------------------------");

        for (Bidder b : list) {
            System.out.printf("| %-12s | %-12s | %-10.2f |\n",
                    b.getName(),
                    b.getType(),
                    b.getBudget());
        }

        System.out.println("-------------------------------------------------");
    }

    // ================= UTILS =================

    private String getColor(int i) {
        return ConsoleColors.BOLD +
                ConsoleColors.ALL_COLORS[i % ConsoleColors.ALL_COLORS.length];
    }

    private void waitForEnter() {
        System.out.println("\n⏳ Enter drücken...");
        scanner.nextLine();
    }

    private void pushUp() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}