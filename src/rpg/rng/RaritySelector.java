package rpg.rng;

import rpg.model.Rarity;

public class RaritySelector {
    private final RandomNumberGenerator rng;

    public RaritySelector(RandomNumberGenerator rng) {
        this.rng = rng;
    }

    public Rarity selectRarity() {
        int roll = rng.getRandomInt(1, 100);
        if (roll <= 70) return Rarity.COMMON;
        if (roll <= 90) return Rarity.RARE;
        if (roll <= 98) return Rarity.EPIC;
        return Rarity.LEGENDARY;
    }
}
