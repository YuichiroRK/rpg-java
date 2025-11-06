package rpg.rng;

import rpg.model.*;
import java.util.List;

public class CardSelector {
    private final CardPool pool;
    private final RandomNumberGenerator rng;

    public CardSelector(CardPool pool, RandomNumberGenerator rng) {
        this.pool = pool;
        this.rng = rng;
    }

    public Card selectCard(Rarity rarity) {
        List<Card> available = pool.getCardsByRarity(rarity);
        if (available.isEmpty()) return null;
        int index = rng.getRandomInt(0, available.size() - 1);
        return available.get(index);
    }
}
