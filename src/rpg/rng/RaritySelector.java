package rpg.rng;

import rpg.model.Rarity;
import java.util.*;

public class RaritySelector {
    private final RandomNumberGenerator rng;
    private final List<Rarity> rarities;
    private final double totalWeight;

    public RaritySelector(RandomNumberGenerator rng) {
        this.rng = rng;
        this.rarities = Arrays.asList(Rarity.values());
        this.totalWeight = rarities.stream().mapToDouble(Rarity::getWeight).sum();
    }

    public Rarity selectRarity() {
        double roll = rng.nextDouble() * totalWeight;
        double cumulative = 0;

        for (Rarity rarity : rarities) {
            cumulative += rarity.getWeight();
            if (roll <= cumulative) {
                return rarity;
            }
        }
        return Rarity.COMMON;
    }
}
