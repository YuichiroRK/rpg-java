package rpg.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rpg.model.Card;
import rpg.rng.*;

import java.util.*;

public class RNGController {

    private final RNGSystem rngSystem;
    private final Label lblResultado;

    private final List<Card> playerCards = new ArrayList<>();
    private final List<Card> catalog = new ArrayList<>();

    public RNGController() {

        RandomNumberGenerator rng = new RandomNumberGenerator();
        CardPool pool = new CardPool();
        RaritySelector raritySelector = new RaritySelector(rng);
        CardSelector cardSelector = new CardSelector(pool, rng);
        this.rngSystem = new RNGSystem(raritySelector, cardSelector);

        lblResultado = new Label("Presiona un botón para empezar.");
        lblResultado.setTextFill(Color.WHITE);
    }

    public VBox createView() {

        Label title = new Label("🎴 Sistema RNG de Cartas");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: gold;");

        Button btnObtener = new Button("🎲 Obtener carta");
        btnObtener.setOnAction(e -> drawCard());

        Button btnVerInventario = new Button("📜 Ver mis cartas");
        btnVerInventario.setOnAction(e -> showCards(playerCards, "Tus cartas"));

        Button btnVerCatalogo = new Button("📚 Ver catálogo");
        btnVerCatalogo.setOnAction(e -> showCards(catalog, "Catálogo"));

        VBox box = new VBox(20, title, btnObtener, btnVerInventario, btnVerCatalogo, lblResultado);
        box.setStyle("-fx-alignment: center; -fx-padding: 40px; -fx-background-color: #202020;");

        return box;
    }

    // ============================================================
    //  OBTENER CARTA Y MOSTRARLA VISUALMENTE
    // ============================================================
    private void drawCard() {
        Card card = rngSystem.drawCard();

        playerCards.add(card);
        catalog.add(card);

        lblResultado.setText("Obtuviste: " + card.getName());
        lblResultado.setStyle("-fx-text-fill: " + card.getRarity().getColorHex());

        showCardPopup(card);
    }

    private void showCardPopup(Card card) {
        Stage stage = new Stage();
        VBox box = new VBox(new CardView(card));
        box.setStyle("-fx-padding: 20; -fx-background-color: #1c1c1c;");

        stage.setScene(new Scene(box, 300, 500));
        stage.setTitle("Nueva carta obtenida");
        stage.show();
    }

    // ============================================================
    //  MOSTRAR LISTA VISUAL DE CARTAS
    // ============================================================
    private void showCards(List<Card> list, String title) {

        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-background-color: #111;");

        if (list.isEmpty()) {
            root.getChildren().add(new Label("No hay cartas aún."));
        } else {
            for (Card card : list) {
                root.getChildren().add(new CardView(card));
            }
        }

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);

        stage.setScene(new Scene(scroll, 350, 600));
        stage.setTitle(title);
        stage.show();
    }
}
