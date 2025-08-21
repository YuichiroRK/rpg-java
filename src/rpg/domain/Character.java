package rpg.domain;

public class Character {
    private String name;
    private Stats stats;
    private Inventory inventory;

    public Character(String name, Stats stats) {
        this.name = name;
        this.stats = stats;
        this.inventory = new Inventory();
    }

    public String getName() { return name; }
    public Stats getStats() { return stats; }
    public Inventory getInventory() { return inventory; }
}

