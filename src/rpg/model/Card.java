package rpg.model;

public class Card {
    private final String name;
    private final Rarity rarity;
    private final int power;
    private String imagePath; // ← nueva ruta de imagen

    public Card(String name, Rarity rarity, int power, String imagePath) {
        this.name = name;
        this.rarity = rarity;
        this.power = power;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public int getPower() { return power; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() {
        return name + " (" + rarity + ", power " + power + ")";
    }
}
