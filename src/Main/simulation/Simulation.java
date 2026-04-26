package simulation;

import model.*;
import service.*;

/**
 * Einstiegspunkt der Anwendung.
 *
 * Diese Klasse enthält die main-Methode und startet das Programm.
 *
 * Ablauf:
 * - Begrüßungsausgabe im Terminal
 * - Start des Menüsystems
 */
public class Simulation {

    /**
     * Startpunkt des Programms.
     *
     * @param args Kommandozeilenargumente (werden hier nicht verwendet)
     */
    public static void main(String[] args) {

        System.out.println("\n=================================");
        System.out.println("\n " +
                "AUKTIONS SIMULATION GESTARTET!\n");

        // Start des Menüs
        Menu menu = new Menu();
        menu.start();
    }
}