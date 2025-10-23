import chucknorris.ChuckNorrisAdapter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.*;

public class MainFX extends Application {

    private ProgressBar sqliteProgress = new ProgressBar(0);
    private ProgressBar mysqlProgress = new ProgressBar(0);
    private ProgressBar postgresProgress = new ProgressBar(0);
    private TextArea jokeArea = new TextArea();

    @Override
    public void start(Stage stage) {
        Label title = new Label("💾 Gestor de Chistes Chuck Norris");
        title.setFont(new Font("Segoe UI Semibold", 24));
        title.setTextFill(Color.web("#00D4FF"));

        Label lblSQLite = new Label("SQLite:");
        Label lblMySQL = new Label("MySQL:");
        Label lblPostgres = new Label("PostgreSQL:");
        lblSQLite.setTextFill(Color.WHITE);
        lblMySQL.setTextFill(Color.WHITE);
        lblPostgres.setTextFill(Color.WHITE);

        sqliteProgress.setPrefWidth(250);
        mysqlProgress.setPrefWidth(250);
        postgresProgress.setPrefWidth(250);

        VBox connectionBox = new VBox(10, lblSQLite, sqliteProgress, lblMySQL, mysqlProgress, lblPostgres, postgresProgress);
        connectionBox.setPadding(new Insets(20, 0, 20, 0));

        Button btnConnect = new Button("🚀 Obtener y guardar chiste");
        btnConnect.setStyle("-fx-background-color: #00D4FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnConnect.setOnAction(e -> connectAndInsert());

        Button btnSQLite = crearBotonBD("🟣 Chistes SQLite");
        Button btnMySQL = crearBotonBD("🟠 Chistes MySQL");
        Button btnPostgres = crearBotonBD("🟢 Chistes PostgreSQL");

        btnSQLite.setOnAction(e -> abrirVentanaChistes("SQLite"));
        btnMySQL.setOnAction(e -> abrirVentanaChistes("MySQL"));
        btnPostgres.setOnAction(e -> abrirVentanaChistes("PostgreSQL"));

        HBox botonesBD = new HBox(15, btnSQLite, btnMySQL, btnPostgres);
        botonesBD.setAlignment(Pos.CENTER);

        jokeArea.setEditable(false);
        jokeArea.setPrefHeight(180);
        jokeArea.setWrapText(true);
        jokeArea.setStyle("""
            -fx-control-inner-background: #222;
            -fx-font-family: 'Consolas';
            -fx-font-size: 14px;
            -fx-text-fill: #00D4FF;
            -fx-border-color: #00D4FF;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            """);

        VBox root = new VBox(20,
                title,
                connectionBox,
                btnConnect,
                botonesBD,
                new Label("📜 Último chiste obtenido:"),
                jokeArea
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Chuck Norris App ⚡");
        stage.setScene(scene);
        stage.show();
    }

    private Button crearBotonBD(String texto) {
        Button boton = new Button(texto);
        boton.setStyle("""
            -fx-background-color: #333;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 12;
            -fx-border-color: #00D4FF;
            -fx-border-width: 1;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            """);
        boton.setOnMouseEntered(e -> boton.setStyle("""
            -fx-background-color: #00D4FF;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 12;
            -fx-border-color: transparent;
            """));
        boton.setOnMouseExited(e -> boton.setStyle("""
            -fx-background-color: #333;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 12;
            -fx-border-color: #00D4FF;
            -fx-border-width: 1;
            -fx-border-radius: 12;
            """));
        return boton;
    }

    private void connectAndInsert() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 1);

                Database sqliteDb = new SQLiteFactory().createDatabase("jdbc:sqlite:baseproduccion.db");
                createTable(sqliteDb);
                updateProgress(0.2, 1);

                Database mysqlDb = new MySQLFactory().createDatabase("jdbc:mysql://172.30.16.154:3306/construccion1");
                createTable(mysqlDb);
                updateProgress(0.4, 1);

                Database postgresDb = new PostgresFactory().createDatabase("jdbc:postgresql://172.30.16.157:5432/construccion1");
                createTable(postgresDb);
                updateProgress(0.6, 1);

                ChuckNorrisAdapter adapter = new ChuckNorrisAdapter();
                String joke = adapter.getJoke();
                updateMessage(joke);

                insertJoke(sqliteDb, joke);
                insertJoke(mysqlDb, joke);
                insertJoke(postgresDb, joke);

                updateProgress(1, 1);
                return null;
            }
        };

        sqliteProgress.progressProperty().bind(task.progressProperty());
        mysqlProgress.progressProperty().bind(task.progressProperty());
        postgresProgress.progressProperty().bind(task.progressProperty());
        jokeArea.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    private void createTable(Database db) {
        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS ChistesChabes (
                AUTOID INTEGER PRIMARY KEY AUTO_INCREMENT,
                chiste_en_ingles TEXT,
                chiste_en_espanol TEXT
            )
        """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertJoke(Database db, String joke) {
        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement()) {

            String[] partes = joke.split("\n");
            if (partes.length >= 2) {
                String ingles = partes[0].replace("Inglés: ", "").replace("'", "''");
                String espanol = partes[1].replace("Español: ", "").replace("'", "''");

                stmt.execute("INSERT INTO ChistesChabes (chiste_en_ingles, chiste_en_espanol) " +
                        "VALUES ('" + ingles + "', '" + espanol + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaChistes(String tipoBD) {
        Database db;
        String query = "SELECT * FROM ChistesChabes";

        switch (tipoBD) {
            case "SQLite" -> db = new SQLiteFactory().createDatabase("jdbc:sqlite:baseproduccion.db");
            case "MySQL" -> db = new MySQLFactory().createDatabase("jdbc:mysql://172.30.16.157:3306/construccion1");
            case "PostgreSQL" -> db = new PostgresFactory().createDatabase("jdbc:postgresql://172.30.16.157:5432/construccion1");
            default -> {
                System.out.println("Tipo de BD no reconocido");
                return;
            }
        }

        Stage stage = new Stage();
        TableView<Chistes> tableView = new TableView<>();

        TableColumn<Chistes, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Chistes, String> colIngles = new TableColumn<>("Chiste en Inglés");
        colIngles.setCellValueFactory(new PropertyValueFactory<>("chisteEnIngles"));

        TableColumn<Chistes, String> colEspanol = new TableColumn<>("Chiste en Español");
        colEspanol.setCellValueFactory(new PropertyValueFactory<>("chisteEnEspanol"));

        tableView.getColumns().addAll(colId, colIngles, colEspanol);

        ObservableList<Chistes> data = FXCollections.observableArrayList();
        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new Chistes(
                        rs.getInt("AUTOID"),
                        rs.getString("chiste_en_ingles"),
                        rs.getString("chiste_en_espanol")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);

        Button btnEditar = new Button("✏️ Editar");
        Button btnEliminar = new Button("🗑️ Eliminar");

        btnEditar.setOnAction(e -> editarChiste(db, tableView));
        btnEliminar.setOnAction(e -> eliminarChiste(db, tableView));

        HBox botones = new HBox(15, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        VBox layout = new VBox(10, tableView, botones);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.setTitle("📚 Chistes " + tipoBD);
        stage.show();
    }

    private void editarChiste(Database db, TableView<Chistes> tableView) {
        Chistes seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un chiste para editar", Alert.AlertType.WARNING);
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Editar Chiste");
        dialog.setHeaderText("Modificar chiste en inglés y español");

        Label lblEn = new Label("Inglés:");
        Label lblEs = new Label("Español:");
        TextField txtEn = new TextField(seleccionado.getChisteEnIngles());
        TextField txtEs = new TextField(seleccionado.getChisteEnEspanol());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(lblEn, 0, 0);
        grid.add(txtEn, 1, 0);
        grid.add(lblEs, 0, 1);
        grid.add(txtEs, 1, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return new Pair<>(txtEn.getText(), txtEs.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            try (Connection conn = db.connect();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE ChistesChabes SET chiste_en_ingles=?, chiste_en_espanol=? WHERE AUTOID=?")) {
                ps.setString(1, pair.getKey());
                ps.setString(2, pair.getValue());
                ps.setInt(3, seleccionado.getId());
                ps.executeUpdate();

                seleccionado.setChisteEnIngles(pair.getKey());
                seleccionado.setChisteEnEspanol(pair.getValue());
                tableView.refresh();

            } catch (SQLException ex) {
                ex.printStackTrace();
                mostrarAlerta("Error al actualizar el chiste", Alert.AlertType.ERROR);
            }
        });
    }

    private void eliminarChiste(Database db, TableView<Chistes> tableView) {
        Chistes seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selecciona un chiste para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que deseas eliminar este chiste?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            try (Connection conn = db.connect();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM ChistesChabes WHERE AUTOID=?")) {
                ps.setInt(1, seleccionado.getId());
                ps.executeUpdate();

                tableView.getItems().remove(seleccionado);
            } catch (SQLException ex) {
                ex.printStackTrace();
                mostrarAlerta("Error al eliminar el chiste", Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}

// === Clase auxiliar para la tabla ===
class Chiste {
    private int id;
    private String chisteEnIngles;
    private String chisteEnEspanol;


    public Chiste(int id, String chisteEnIngles, String chisteEnEspanol) {
        this.id = id;
        this.chisteEnIngles = chisteEnIngles;
        this.chisteEnEspanol = chisteEnEspanol;
    }

    public int getId() { return id; }
    public String getChisteEnIngles() { return chisteEnIngles; }
    public String getChisteEnEspanol() { return chisteEnEspanol; }
    public void setChisteEnIngles(String chisteEnIngles) { this.chisteEnIngles = chisteEnIngles; }
    public void setChisteEnEspanol(String chisteEnEspanol) { this.chisteEnEspanol = chisteEnEspanol; }


}
