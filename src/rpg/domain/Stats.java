package rpg.domain;

public class Stats {
    private int health;
    private int attack;
    private int defense;

    public Stats(int health, int attack, int defense) {
        this.health = health;
        this.attack = attack;
        this.defense = defense;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
}
