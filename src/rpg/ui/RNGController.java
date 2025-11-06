package rpg.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import database.Database;
import database.DatabaseFactory;
import database.SQLiteFactory;

import rpg.db.CardRepository;
import rpg.model.Card;
import rpg.rng.*;

public class RNGController {
    private final RNGSystem rngSystem;
    private final CardRepository repository;
    private final Label lblResultado;

    public RNGController() {
        // Sistema RNG
        RandomNumberGenerator rng = new RandomNumberGenerator();
        CardPool pool = new CardPool();
        RaritySelector raritySelector = new RaritySelector(rng);
        CardSelector cardSelector = new CardSelector(pool, rng);
        this.rngSystem = new RNGSystem(raritySelector, cardSelector);

        // Conexión BD
        DatabaseFactory factory = new SQLiteFactory();
        Database database = factory.createDatabase("jdbc:sqlite:baseproduccion.db");
        Connection conn = database.connect();
        this.repository = new CardRepository(conn);
        repository.createTablesIfNotExists();

        this.lblResultado = new Label("Presiona un botón para empezar.");
        lblResultado.setTextFill(Color.WHITE);
        lblResultado.setStyle("-fx-font-size: 14px;");
    }

    public VBox createView() {
        Label title = new Label("🎴 Sistema RNG de Cartas");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: gold;");

        Button btnObtener = new Button("🎲 Obtener carta");
        btnObtener.setStyle("-fx-font-size: 14px;");
        btnObtener.setOnAction(e -> drawCard());

        Button btnVerInventario = new Button("📜 Ver mis cartas");
        btnVerInventario.setStyle("-fx-font-size: 14px;");
        btnVerInventario.setOnAction(e -> showMyCards());

        Button btnVerCatalogo = new Button("📚 Ver catálogo");
        btnVerCatalogo.setStyle("-fx-font-size: 14px;");
        btnVerCatalogo.setOnAction(e -> showCatalog());

        VBox box = new VBox(20, title, btnObtener, btnVerInventario, btnVerCatalogo, lblResultado);
        box.setStyle("-fx-alignment: center; -fx-padding: 40px; -fx-background-color: #202020;");
        return box;
    }

    private void drawCard() {
        Card card = rngSystem.drawCard();
        repository.saveToCatalog(card);      // guarda en el catálogo global
        repository.saveCardForPlayer(card);  // guarda en inventario del jugador

        lblResultado.setText("Has obtenido: " + card.getName() +
                "\nRareza: " + card.getRarity() +
                "\nPoder: " + card.getPower());
    }

    // 📜 Mostrar cartas del jugador
    private void showMyCards() {
        List<String> cards = repository.getPlayerCards();
        showCardsInDialog("Tus cartas actuales", "Colección de jugador", cards);
    }

    // 📚 Mostrar catálogo completo
    private void showCatalog() {
        List<String> cards = getAllCardsFromCatalog();
        showCardsInDialog("Catálogo de cartas", "Todas las cartas disponibles", cards);
    }

    // 💬 Muestra las cartas en un diálogo con colores por rareza
    private void showCardsInDialog(String title, String header, List<String> cards) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        VBox content = new VBox(5);
        content.setStyle("-fx-padding: 10;");

        if (cards.isEmpty()) {
            content.getChildren().add(new Label("No hay cartas registradas."));
        } else {
            for (String card : cards) {
                Text text = new Text(card + "\n");
                text.setFill(getColorForRarity(card));
                text.setFont(Font.font("Consolas", 14));
                content.getChildren().add(text);
            }
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        dialog.getDialogPane().setContent(scrollPane);

        ButtonType closeButton = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        dialog.showAndWait();
    }

    // 🎨 Color por rareza
    private Color getColorForRarity(String cardText) {
        if (cardText.contains("LEGENDARY") || cardText.contains("Legendary")) return Color.GOLD;
        if (cardText.contains("EPIC") || cardText.contains("Epic")) return Color.MEDIUMPURPLE;
        if (cardText.contains("RARE") || cardText.contains("Rare")) return Color.DEEPSKYBLUE;
        if (cardText.contains("COMMON") || cardText.contains("Common")) return Color.LIGHTGRAY;
        return Color.WHITE;
    }

    // 📖 Obtiene todas las cartas del catálogo general
    private List<String> getAllCardsFromCatalog() {
        List<String> catalog = new java.util.ArrayList<>();
        Connection conn = repository.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, rarity, power FROM cards ORDER BY rarity DESC, name")) {

            while (rs.next()) {
                catalog.add(rs.getString("name") + " (" +
                        rs.getString("rarity") + ", Poder: " +
                        rs.getInt("power") + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return catalog;
    }
}
