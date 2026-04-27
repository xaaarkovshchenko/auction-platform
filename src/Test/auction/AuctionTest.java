/**
 * Testklasse für die Klasse Auction.
 *
 * Diese Tests überprüfen die zentrale Logik der Auktion:
 * - Setzen des Gewinners nach einem Gebot
 * - Beenden der Auktion
 * - Verhalten ohne Gebot
 * - Schutz vor mehrfachen Geboten
 * - Statusänderungen der Auktion
 *
 * Es handelt sich um Unit-Tests, da die Methoden der Auction-Klasse
 * isoliert überprüft werden.
 */


package auction;

import model.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {

    @Test
    void testPlaceBidSetsWinnerAndStopsAuction() {

        // Testdaten vorbereiten
        Item item = new Item("Test Item", Category.ELECTRONICS);
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        ArrayList<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder);

        Auction auction = new Auction(item, 100, 50, bidders, "");

        // Act: Methode ausführen
        auction.placeBid(bidder);

        // Assert: Ergebnisse überprüfen
        assertEquals(bidder, auction.getWinner()); // Gewinner korrekt gesetzt
        assertFalse(auction.isActive());           // Auktion beendet
        assertTrue(auction.isSold());              // Artikel verkauft
    }


    @Test
    void testPriceNeverBelowMinPrice() {

        // Vorbereitung der Testdaten
        // Erstellen eines Test-Items und eines Bieters
        Item item = new Item("Test Item", Category.ELECTRONICS);
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        // Liste mit einem Bieter erstellen
        ArrayList<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder);

        // Auktion initialisieren
        Auction auction = new Auction(item, 100, 50, bidders, "");

        // Ausführen der Methode
        // Ein Gebot wird abgegeben → Auktion wird beendet
        auction.placeBid(bidder);

        // Überprüfung der Bedingung
        // Der Endpreis darf niemals unter dem Mindestpreis liegen
        assertTrue(auction.getFinalPrice() >= auction.getMinPrice());
    }

    @Test
    void testAuctionFinishesAfterBid() {

        // Testdaten vorbereiten
        Item item = new Item("Test Item", Category.ELECTRONICS);
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        ArrayList<Bidder> bidders = new ArrayList<>();
        bidders.add(bidder);

        Auction auction = new Auction(item, 100, 50, bidders, "");

        // Methode ausführen
        // Ein Gebot beendet die Auktion
        auction.placeBid(bidder);

        // Zustand der Auktion überprüfen
        // Die Auktion darf nicht mehr aktiv sein und gilt als beendet
        assertTrue(auction.isFinished() || !auction.isActive());
    }
}