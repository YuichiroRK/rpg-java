package rpg.model;

public class Card {
    private final String name;
    private final Rarity rarity;
    private final int power;

    public Card(String name, Rarity rarity, int power) {
        this.name = name;
        this.rarity = rarity;
        this.power = power;
    }

    public String getName() { return name; }
    public Rarity getRarity() { return rarity; }
    public int getPower() { return power; }

    @Override
    public String toString() {
        return name + " (" + rarity + ", power " + power + ")";
    }
}
