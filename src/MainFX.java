import chucknorris.ChuckNorrisAdapter;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;

public class MainFX extends Application {

    private ProgressBar sqliteProgress = new ProgressBar(0);
    private ProgressBar mysqlProgress = new ProgressBar(0);
    private ProgressBar postgresProgress = new ProgressBar(0);
    private TextArea jokeArea = new TextArea();

    @Override
    public void start(Stage stage) {
        // === Título principal ===
        Label title = new Label("💾 Gestor de Chistes Chuck Norris");
        title.setFont(new Font("Segoe UI Semibold", 24));
        title.setTextFill(Color.web("#00D4FF"));

        // === Sección de progreso de conexiones ===
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

        // === Botón de conexión ===
        Button btnConnect = new Button("🚀 Obtener y guardar chiste");
        btnConnect.setStyle("-fx-background-color: #00D4FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 10;");
        btnConnect.setOnAction(e -> connectAndInsert());

        // === Botones de base de datos ===
        Button btnSQLite = crearBotonBD("🟣 Chistes SQLite");
        Button btnMySQL = crearBotonBD("🟠 Chistes MySQL");
        Button btnPostgres = crearBotonBD("🟢 Chistes PostgreSQL");

        btnSQLite.setOnAction(e -> abrirVentanaChistes("SQLite"));
        btnMySQL.setOnAction(e -> abrirVentanaChistes("MySQL"));
        btnPostgres.setOnAction(e -> abrirVentanaChistes("PostgreSQL"));

        HBox botonesBD = new HBox(15, btnSQLite, btnMySQL, btnPostgres);
        botonesBD.setAlignment(Pos.CENTER);

        // === Área de chiste ===
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

        // === Layout principal ===
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

                // === SQLite ===
                Database sqliteDb = new SQLiteFactory().createDatabase("jdbc:sqlite:baseproduccion.db");
                createTable(sqliteDb);
                updateProgress(0.2, 1);

                // === MySQL ===
                Database mysqlDb = new MySQLFactory().createDatabase("jdbc:mysql://localhost:3306/construccion1");
                createTable(mysqlDb);
                updateProgress(0.4, 1);

                // === PostgreSQL ===
                Database postgresDb = new PostgresFactory().createDatabase("jdbc:postgresql://172.30.16.45:5432/construccion1");
                createTable(postgresDb);
                updateProgress(0.6, 1);

                // === Obtener chiste de Chuck Norris ===
                ChuckNorrisAdapter adapter = new ChuckNorrisAdapter();
                String joke = adapter.getJoke();
                updateMessage(joke);

                // === Insertar en las tres bases ===
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
            case "MySQL" -> db = new MySQLFactory().createDatabase("jdbc:mysql://localhost:3306/construccion1");
            case "PostgreSQL" -> db = new PostgresFactory().createDatabase("jdbc:postgresql://172.30.16.45:5432/construccion1");
            default -> {
                System.out.println("Tipo de BD no reconocido");
                return;
            }
        }

        MainUtils.abrirVentanaChistes("📚 Chistes " + tipoBD, db, query);
    }

    public static void main(String[] args) {
        launch();
    }
}
