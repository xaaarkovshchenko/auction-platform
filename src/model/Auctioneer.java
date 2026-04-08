package model;

public class Auctioneer extends User implements Runnable {

    public Auctioneer(int id, String name) {
        super(id, name);
    }

    @Override
    public void run() {
        // kann erweitert werden
    }
}