package rpg.persistence;

import rpg.domain.Character;

public interface CharacterRepository {
    void save(Character character);
    Character load(String name);
}
