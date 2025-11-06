package rpg.rng;

import rpg.model.Card;
import rpg.model.Rarity;
import java.util.*;

public class CardPool {
    private final Map<Rarity, List<Card>> cardsByRarity = new HashMap<>();

    public CardPool() {
        for (Rarity rarity : Rarity.values()) {
            cardsByRarity.put(rarity, new ArrayList<>());
        }

        // Ejemplos (puedes reemplazar por tus propias cartas)
        cardsByRarity.get(Rarity.COMMON).add(new Card("Forest Sprite", Rarity.COMMON, 20));
        cardsByRarity.get(Rarity.UNCOMMON).add(new Card("Bronze Guardian", Rarity.UNCOMMON, 60));
        cardsByRarity.get(Rarity.RARE).add(new Card("Abyss Mage", Rarity.RARE, 100));
        cardsByRarity.get(Rarity.EPIC).add(new Card("Crimson Knight", Rarity.EPIC, 150));
        cardsByRarity.get(Rarity.LEGENDARY).add(new Card("Celestial Dragon", Rarity.LEGENDARY, 220));
        cardsByRarity.get(Rarity.MYTHIC).add(new Card("Soul Reaver", Rarity.MYTHIC, 300));
        cardsByRarity.get(Rarity.ANCIENT).add(new Card("Time Weaver", Rarity.ANCIENT, 450));
        cardsByRarity.get(Rarity.DIVINE).add(new Card("Aether Seraph", Rarity.DIVINE, 600));
        cardsByRarity.get(Rarity.SECRET).add(new Card("Chrono Demon", Rarity.SECRET, 900));
        cardsByRarity.get(Rarity.ULTIMATE).add(new Card("Oblivion Monarch", Rarity.ULTIMATE, 1300));
    }

    public List<Card> getCardsByRarity(Rarity rarity) {
        return cardsByRarity.getOrDefault(rarity, new ArrayList<>());
    }
}
