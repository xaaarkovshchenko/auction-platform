package model;

public class Item {
    private String name;
    private Category category;

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "📦 Item: " + name + " | Kategorie: " + category;
    }
}