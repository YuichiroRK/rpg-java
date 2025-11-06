package rpg.rng;

import rpg.model.*;
import java.util.*;

public class CardPool {
    private final List<Card> cards = new ArrayList<>();

    public CardPool() {
        cards.add(new Card("Fire Knight", Rarity.COMMON, 30));
        cards.add(new Card("Water Mage", Rarity.COMMON, 28));
        cards.add(new Card("Earth Guardian", Rarity.RARE, 45));
        cards.add(new Card("Storm Dragon", Rarity.EPIC, 70));
        cards.add(new Card("Celestial Phoenix", Rarity.LEGENDARY, 100));
    }

    public List<Card> getCardsByRarity(Rarity rarity) {
        return cards.stream()
                .filter(c -> c.getRarity() == rarity)
                .toList();
    }
}
