package rpg;

import rpg.domain.Character;
import rpg.domain.Stats;
import rpg.persistence.CharacterRepository;
import rpg.persistence.sqlite.SQLiteCharacterRepository;

public class RPGMain {
    public static void main(String[] args) {
        Character hero = new Character("Julian", new Stats(100, 20, 10));

        CharacterRepository repo = new SQLiteCharacterRepository();
        repo.save(hero);

        Character loaded = repo.load("Julian");

        if (loaded != null) {
            System.out.println("✅ Personaje cargado desde DB:");
            System.out.println("Nombre: " + loaded.getName());
            System.out.println("Vida: " + loaded.getStats().getHealth());
            System.out.println("Ataque: " + loaded.getStats().getAttack());
            System.out.println("Defensa: " + loaded.getStats().getDefense());
        } else {
            System.out.println("❌ No se pudo cargar el personaje");
        }
    }
}
