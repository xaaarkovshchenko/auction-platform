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

            // 🔥 Fix: ungültige Eingabe ignorieren
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

    /**
     * Gibt den Menü-Header und die verfügbaren Optionen aus.
     */
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

    /**
     * Liest die Benutzereingabe und validiert sie.
     *
     * @return gewählte Menüoption oder -1 bei Fehler
     */
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

    /**
     * Startet die Simulation aller aktiven Auktionen.
     */
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

    /**
     * Erstellt eine neue Auktion durch Benutzereingaben.
     */
    private void createAuction() {

        try {
            String name = readName();
            Category category = readCategory();
            double start = readStartPrice();
            double min = readMinPrice(start);

            List<Bidder> bidders = createBidders(name);

            Item item = new Item(name, category);

            int index = house.getAuctions().size();

            Auction auction = new Auction(
                    item,
                    start,
                    min,
                    bidders,
                    getColor(index) //
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

    /**
     * Erstellt automatisch 5 Test-Auktionen.
     */
    private void createTestAuctions() {

        Random random = new Random();

        for (int i = 0; i < 5; i++) {

            String itemName = "Item_" + (i + 1);

            List<Bidder> bidders = createBidders(itemName);

            Item item = new Item(
                    itemName,
                    Category.values()[i % Category.values().length]
            );

            Auction auction = new Auction(
                    item,
                    1000 + random.nextInt(2000),
                    500 + random.nextInt(1000),
                    bidders,
                    getColor(i) //
            );

            house.addAuction(auction);
        }

        System.out.println("\n🔥 5 Test-Auktionen erstellt!");
    }

    // ================= INPUT =================

    private String readName() {
        System.out.print("\n📦 Artikelname: ");
        return scanner.nextLine();
    }

    private Category readCategory() {

        Category[] categories = Category.values();

        System.out.println("\n📂 Kategorie wählen:");

        for (int i = 0; i < categories.length; i++) {
            System.out.println(i + " -> " + categories[i]);
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        return categories[Math.max(0, Math.min(choice, categories.length - 1))];
    }

    private double readStartPrice() {
        System.out.print("💰 Startpreis: ");
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    private double readMinPrice(double start) {
        System.out.print("🔻 Mindestpreis: ");
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    // ================= BIDDERS =================

    private List<Bidder> createBidders(String itemName) {

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

    /**
     * Gibt eine Farbe basierend auf Index zurück.
     * Nutzt alle verfügbaren Farben aus ConsoleColors.
     */
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