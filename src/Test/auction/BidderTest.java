/**
 * Testklasse für die Klasse Bidder.
 *
 * Diese Tests überprüfen die Funktionalität eines Bieters:
 * - Reduzierung des Budgets nach einem Kauf
 * - Korrekte Berechnung nach mehreren Änderungen
 *
 * Der Fokus liegt auf der internen Logik des Bieters
 * unabhängig von der Auction-Klasse.
 *
 * Es handelt sich um Unit-Tests.
 */

package auction;

import model.Bidder;
import model.BidderType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BidderTest {

    @Test
    void testDecreaseBudget() {

        // Bieter erstellen
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        // Budget reduzieren
        bidder.decreaseBudget(200);

        // Neues Budget prüfen
        assertEquals(800, bidder.getBudget());
    }

    @Test
    void testBudgetAfterMultipleReductions() {

        // Arrange
        Bidder bidder = new Bidder(1, "Max", 1000, BidderType.AGGRESSIVE);

        // Act
        bidder.decreaseBudget(300);
        bidder.decreaseBudget(200);

        // Assert
        assertEquals(500, bidder.getBudget());
    }
}
