package rpg.rng;

import rpg.model.*;

public class RNGSystem {
    private final RaritySelector raritySelector;
    private final CardSelector cardSelector;

    public RNGSystem(RaritySelector raritySelector, CardSelector cardSelector) {
        this.raritySelector = raritySelector;
        this.cardSelector = cardSelector;
    }

    public Card drawCard() {
        Rarity rarity = raritySelector.selectRarity();
        return cardSelector.selectCard(rarity);
    }
}
