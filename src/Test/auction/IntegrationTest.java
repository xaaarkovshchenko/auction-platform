/**
 * Integrationstests für das Zusammenspiel mehrerer Klassen.
 *
 * Diese Tests überprüfen die Interaktion zwischen:
 * - Auction
 * - Bidder
 * - Item
 *
 * Getestet werden u.a.:
 * - Erfolgreiches Gebot eines Bieters
 * - Setzen des Gewinners
 * - Reduzierung des Budgets
 * - Verhalten bei mehreren Bietern
 *
 * Im Gegensatz zu Unit-Tests werden hier mehrere Komponenten
 * gemeinsam getestet, um das Gesamtsystem zu validieren.
 */

package auction;

import model.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    void testBidderWinsAuctionAndBudgetIsReduced() {

        // Daten vorbereiten
        Item item = new Item("Laptop", Category.ELECTRONICS);
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        ArrayList<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder);

        Auction auction = new Auction(item, 100, 50, bidders, "");

        double budgetBefore = bidder.getBudget();

        // Bieter gibt Gebot ab
        auction.placeBid(bidder);

        // Gewinner gesetzt
        assertEquals(bidder, auction.getWinner());

        // Budget wurde reduziert
        assertTrue(bidder.getBudget() < budgetBefore);

        // Auktion beendet
        assertFalse(auction.isActive());
    }


    //Dieser Test überprüft das Verhalten der Auktion mit mehreren Bietern.
    @Test
    void testMultipleBiddersOneWins() {

        // Arrange: mehrere Bieter erstellen
        Item item = new Item("Phone", Category.ELECTRONICS);

        Bidder bidder1 = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);
        Bidder bidder2 = new Bidder(2, "Anna", 800, BidderType.CONSERVATIVE);

        ArrayList<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder1);
        bidders.add(bidder2);

        Auction auction = new Auction(item, 100, 50, bidders, "");

        // ein Bieter gibt Gebot ab
        auction.placeBid(bidder2);

        // richtiger Gewinner
        assertEquals(bidder2, auction.getWinner());

        // Auktion ist beendet
        assertFalse(auction.isActive());

        // Nur dieser Bieter hat Budget verloren
        assertTrue(bidder2.getBudget() < 800);
        assertEquals(1000, bidder1.getBudget());
    }
}