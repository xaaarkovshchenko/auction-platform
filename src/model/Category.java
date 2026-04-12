package model;

public enum Category {
    ELECTRONICS("📱 Electronics"),
    CARS("🚗 Cars"),
    BOOKS("📚 Books"),
    FASHION("👕 Fashion"),
    SPORT("⚽ Sport");

    private String label;

    Category(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}