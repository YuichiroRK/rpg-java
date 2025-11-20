package rpg.rng;

import rpg.model.Card;
import rpg.model.Rarity;

import java.util.*;

public class CardPool {

    private final Map<Rarity, List<Card>> cardsByRarity = new EnumMap<>(Rarity.class);

    public CardPool() {
        cardsByRarity.put(Rarity.COMMON, new ArrayList<>());
        cardsByRarity.put(Rarity.RARE, new ArrayList<>());
        cardsByRarity.put(Rarity.EPIC, new ArrayList<>());
        cardsByRarity.put(Rarity.LEGENDARY, new ArrayList<>());

        loadCards();
    }

    private void loadCards() {

        // COMMON
        cardsByRarity.get(Rarity.COMMON).add(new Card("Forest Sprite", Rarity.COMMON, 20, "/cards/Forest Sprite.png"));
        cardsByRarity.get(Rarity.COMMON).add(new Card("Village Guard", Rarity.COMMON, 18, "/cards/Village Guard.png"));
        cardsByRarity.get(Rarity.COMMON).add(new Card("Grave Digger", Rarity.COMMON, 15, "/cards/Grave Digger.png"));
        cardsByRarity.get(Rarity.COMMON).add(new Card("Healing Potion", Rarity.COMMON, 10, "/cards/Healing Potion.png"));
        cardsByRarity.get(Rarity.COMMON).add(new Card("Stone Wall", Rarity.COMMON, 8, "/cards/Stone Wall.png"));

        // RARE
        cardsByRarity.get(Rarity.RARE).add(new Card("Bronze Guardian", Rarity.RARE, 40, "/cards/Bronze Guardian.png"));
        cardsByRarity.get(Rarity.RARE).add(new Card("Treasure Hunter", Rarity.RARE, 35, "/cards/Treasure Hunter.png"));
        cardsByRarity.get(Rarity.RARE).add(new Card("Elemental Bolt", Rarity.RARE, 30, "/cards/Elemental Bolt.png"));
        cardsByRarity.get(Rarity.RARE).add(new Card("Wind Seeker", Rarity.RARE, 32, "/cards/Wind Seeker.png"));

        // EPIC
        cardsByRarity.get(Rarity.EPIC).add(new Card("Steel Sentinel", Rarity.EPIC, 60, "/cards/Steel Sentinel.png"));
        cardsByRarity.get(Rarity.EPIC).add(new Card("Soul Reaver", Rarity.EPIC, 55, "/cards/Soul Reaver.png"));
        cardsByRarity.get(Rarity.EPIC).add(new Card("Polymorph", Rarity.EPIC, 50, "/cards/Polymorph.png"));
        cardsByRarity.get(Rarity.EPIC).add(new Card("Abyss Mage", Rarity.EPIC, 65, "/cards/Abyss Mage.png"));

        // LEGENDARY
        cardsByRarity.get(Rarity.LEGENDARY).add(new Card("Crimson Knight", Rarity.LEGENDARY, 90, "/cards/Crimson Knight.png"));
        cardsByRarity.get(Rarity.LEGENDARY).add(new Card("Celestial Dragon", Rarity.LEGENDARY, 100, "/cards/Celestial Dragon.png"));
    }

    public List<Card> getCardsByRarity(Rarity rarity) {
        return cardsByRarity.get(rarity);
    }
}
