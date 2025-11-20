package rpg.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import rpg.db.CardRepository;
import rpg.model.Card;
import rpg.model.Rarity;

public class CardEditorView {

    private final CardRepository repository;

    public CardEditorView(CardRepository repository) {
        this.repository = repository;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Agregar nueva carta al catálogo");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b;");

        // 📝 Nombre
        Label lblName = new Label("Nombre de la carta:");
        lblName.setStyle("-fx-text-fill: white;");
        TextField txtName = new TextField();
        txtName.setPromptText("Ej: Soul Reaver");

        // 📝 Descripción
        Label lblDescription = new Label("Descripción:");
        lblDescription.setStyle("-fx-text-fill: white;");
        TextArea txtDescription = new TextArea();
        txtDescription.setPromptText("Ej: Absorbe la esencia del enemigo...");
        txtDescription.setPrefRowCount(3);

        // 🌟 Rareza
        Label lblRarity = new Label("Rareza:");
        lblRarity.setStyle("-fx-text-fill: white;");
        ComboBox<String> cmbRarity = new ComboBox<>();
        cmbRarity.getItems().addAll(
                "Common", "Uncommon", "Rare", "Epic", "Legendary",
                "Mythic", "Ancient", "Divine", "Secret", "Ultimate"
        );
        cmbRarity.getSelectionModel().select("Common");

        // ⚡ Poder
        Label lblPower = new Label("Poder:");
        lblPower.setStyle("-fx-text-fill: white;");
        Slider sldPower = new Slider(0, 50, 10);
        Label lblPowerValue = new Label("10");
        lblPowerValue.setStyle("-fx-text-fill: gold; -fx-font-weight: bold;");

        sldPower.setShowTickLabels(true);
        sldPower.setShowTickMarks(true);
        sldPower.setMajorTickUnit(10);

        sldPower.valueProperty().addListener((obs, o, n) ->
                lblPowerValue.setText(String.valueOf(n.intValue()))
        );

        cmbRarity.valueProperty().addListener((obs, oldVal, newVal) -> {
            switch (newVal.toUpperCase()) {
                case "COMMON" -> sldPower.setMax(50);
                case "UNCOMMON" -> sldPower.setMax(80);
                case "RARE" -> sldPower.setMax(120);
                case "EPIC" -> sldPower.setMax(180);
                case "LEGENDARY" -> sldPower.setMax(250);
                case "MYTHIC" -> sldPower.setMax(350);
                case "ANCIENT" -> sldPower.setMax(500);
                case "DIVINE" -> sldPower.setMax(700);
                case "SECRET" -> sldPower.setMax(1000);
                case "ULTIMATE" -> sldPower.setMax(1500);
            }
            sldPower.setValue(sldPower.getMax() / 2);
            lblPowerValue.setText(String.valueOf((int) sldPower.getValue()));
        });

        // 💾 Guardar
        Button btnGuardar = new Button("Guardar carta");
        btnGuardar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            String rarityName = cmbRarity.getValue();
            int power = (int) sldPower.getValue();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Falta el nombre.");
                return;
            }
            if (desc.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "La descripción no puede estar vacía.");
                return;
            }

            try {
                Rarity rarity = Rarity.valueOf(rarityName.toUpperCase());

                // 🔥 Generar ruta de imagen automática
                String imagePath = "/cards/" + name.replace(" ", "_") + ".png";

                Card card = new Card(name, rarity, power, imagePath);

                repository.saveToCatalog(card);

                showAlert(Alert.AlertType.INFORMATION, "Carta guardada con éxito.");
                txtName.clear();
                txtDescription.clear();

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(
                lblName, txtName,
                lblDescription, txtDescription,
                lblRarity, cmbRarity,
                lblPower, sldPower, lblPowerValue,
                btnGuardar
        );

        stage.setScene(new Scene(root, 350, 550));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
