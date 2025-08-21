package rpg.persistence.sqlite;

import rpg.domain.Character;
import rpg.domain.Stats;
import rpg.persistence.CharacterRepository;

import java.sql.*;

public class SQLiteCharacterRepository implements CharacterRepository {
    private final String url = "jdbc:sqlite:test.db";

    public SQLiteCharacterRepository() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS characters (" +
                    "name TEXT PRIMARY KEY, health INT, attack INT, defense INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Character character) {
        String sql = "INSERT OR REPLACE INTO characters(name, health, attack, defense) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, character.getName());
            pstmt.setInt(2, character.getStats().getHealth());
            pstmt.setInt(3, character.getStats().getAttack());
            pstmt.setInt(4, character.getStats().getDefense());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Character load(String name) {
        String sql = "SELECT health, attack, defense FROM characters WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Stats stats = new Stats(
                        rs.getInt("health"),
                        rs.getInt("attack"),
                        rs.getInt("defense")
                );
                return new Character(name, stats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
