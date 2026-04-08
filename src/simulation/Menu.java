package simulation;

import model.*;
import service.*;

import java.util.*;

public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private AuctionHouse house = AuctionHouse.getInstance();

    public void start() {

        while (true) {
            System.out.println("\n=== AUCTION PLATFORM ===");
            System.out.println("1. Neue Auktion erstellen");
            System.out.println("2. Simulation starten");
            System.out.println("3. Report anzeigen");
            System.out.println("0. Exit");

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

        System.out.println("Artikelname:");
        String name = scanner.nextLine();

        System.out.println("Kategorie wählen:");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println(i + ": " + categories[i]);
        }

        int catChoice = scanner.nextInt();
        Category category = categories[catChoice];

        System.out.println("Startpreis:");
        double start = scanner.nextDouble();

        System.out.println("Mindestpreis:");
        double min = scanner.nextDouble();

        List<Bidder> bidders = createBidders();

        Item item = new Item(name, category);
        Auction auction = new Auction(item, start, min, bidders);

        house.addAuction(auction);

        System.out.println("Auktion erstellt!");
    }

    private List<Bidder> createBidders() {
        List<Bidder> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            list.add(new Bidder(
                    i,
                    "Bidder-" + i,
                    500 + random.nextInt(2000),
                    BidderType.values()[random.nextInt(3)]
            ));
        }

        return list;
    }
}