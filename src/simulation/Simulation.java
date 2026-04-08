package simulation;

import model.*;
import service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    public static void main(String[] args) {

        System.out.println("\n=================================");
        System.out.println("🎮 AUKTIONS SIMULATION GESTARTET");
        System.out.println("=================================\n");

        Menu menu = new Menu();
        menu.start();
    }
}