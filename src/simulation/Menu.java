package simulation;

import model.*;
import service.*;

import java.util.*;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private AuctionHouse house = AuctionHouse.getInstance();

    public void start() {

        while (true) {
            System.out.println("\n=================================");
            System.out.println("🏠 AUCTION PLATFORM");
            System.out.println("=================================");
            System.out.println("1️⃣  Neue Auktion erstellen");
            System.out.println("2️⃣  Simulation starten");
            System.out.println("3️⃣  Report anzeigen");
            System.out.println("0️⃣  Exit");
            System.out.println("=================================");
            System.out.print("👉 Auswahl: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createAuction();
                case 2 -> house.startAll();
                case 3 -> Report.print(house);
                case 0 -> System.exit(0);
            }
        }
    }

    private void createAuction() {

        scanner.nextLine(); // clear buffer

        System.out.print("\n📦 Artikelname eingeben: ");
        String name = scanner.nextLine();

        System.out.println("\n📂 Kategorie wählen:");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println("👉 " + i + " - " + categories[i]);
        }
        System.out.print("Auswahl: ");

        int catChoice = scanner.nextInt();
        Category category = categories[catChoice];

        System.out.print("💰 Startpreis: ");
        double start = scanner.nextDouble();

        System.out.print("🔻 Mindestpreis: ");
        double min = scanner.nextDouble();

        List<Bidder> bidders = createBidders();

        Item item = new Item(name, category);
        Auction auction = new Auction(item, start, min, bidders);

        house.addAuction(auction);

        System.out.println("\n✅ Auktion erfolgreich erstellt!");
        System.out.println("📦 " + item.getName() + " wurde hinzugefügt.\n");
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

        System.out.println("\n👥 Bieter wurden erstellt:");
        for (Bidder b : list) {
            System.out.println("👤 " + b.getName());
        }
        System.out.println();

        return list;
    }
}