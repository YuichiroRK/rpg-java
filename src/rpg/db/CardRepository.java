package rpg.db;

import rpg.model.Card;
import java.sql.*;
import java.util.*;

public class CardRepository {
    private final Connection connection;

    public CardRepository(Connection connection) {
        this.connection = connection;
    }

    // 🔧 Crea ambas tablas si no existen
    public void createTablesIfNotExists() {
        String createCatalog = """
            CREATE TABLE IF NOT EXISTS cards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                rarity TEXT NOT NULL,
                power INTEGER NOT NULL
            );
        """;

        String createPlayer = """
            CREATE TABLE IF NOT EXISTS player_cards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                card_name TEXT NOT NULL,
                rarity TEXT NOT NULL,
                power INTEGER NOT NULL,
                quantity INTEGER DEFAULT 1
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCatalog);
            stmt.execute(createPlayer);
            System.out.println("Tablas 'cards' y 'player_cards' verificadas/creadas correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    // 🗂️ Guarda carta en el catálogo general
    public void saveToCatalog(Card card) {
        String sql = "INSERT OR IGNORE INTO cards (name, rarity, power) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getName());
            stmt.setString(2, card.getRarity().toString());
            stmt.setInt(3, card.getPower());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar carta en catálogo: " + e.getMessage());
        }
    }

    // 💾 Guarda carta en inventario del jugador
    public void saveCardForPlayer(Card card) {
        String checkQuery = "SELECT quantity FROM player_cards WHERE card_name = ?";
        String insertQuery = "INSERT INTO player_cards (card_name, rarity, power, quantity) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE player_cards SET quantity = quantity + 1 WHERE card_name = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, card.getName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, card.getName());
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, card.getName());
                    insertStmt.setString(2, card.getRarity().toString());
                    insertStmt.setInt(3, card.getPower());
                    insertStmt.setInt(4, 1);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // 📋 Muestra cartas del jugador
    public List<String> getPlayerCards() {
        List<String> cards = new ArrayList<>();
        String query = "SELECT card_name, rarity, power, quantity FROM player_cards ORDER BY rarity DESC, card_name";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String rarity = rs.getString("rarity");
                String color = switch (rarity.toUpperCase()) {
                    case "COMMON" -> "⚪";
                    case "RARE" -> "🟦";
                    case "EPIC" -> "🟪";
                    case "LEGENDARY" -> "🟨";
                    default -> "⚫";
                };
                cards.add(color + " " + rs.getString("card_name") +
                        " (" + rarity + ", Poder: " + rs.getInt("power") +
                        ", Copias: " + rs.getInt("quantity") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}

