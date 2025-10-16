// 👈 Ajusta esto según el paquete real de tu clase Database
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainUtils {

    // ✅ Método para abrir una ventana con los chistes desde la base de datos especificada
    public static void abrirVentanaChistes(String titulo, Database database, String query) {
        Stage ventana = new Stage();
        ventana.setTitle(titulo);

        TableView<Chiste> tabla = new TableView<>();

        // Columnas
        TableColumn<Chiste, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        idCol.setPrefWidth(60);

        TableColumn<Chiste, String> enCol = new TableColumn<>("Chiste en Inglés");
        enCol.setCellValueFactory(data -> data.getValue().englishProperty());
        enCol.setPrefWidth(320);

        TableColumn<Chiste, String> esCol = new TableColumn<>("Chiste en Español");
        esCol.setCellValueFactory(data -> data.getValue().spanishProperty());
        esCol.setPrefWidth(320);

        tabla.getColumns().addAll(idCol, enCol, esCol);

        // Llenar datos
        tabla.getItems().addAll(obtenerChistes(database, query));

        // Diseño con modo oscuro
        VBox layout = new VBox(10, tabla);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #121212;");

        tabla.setStyle("""
            -fx-control-inner-background: #1e1e1e;
            -fx-text-fill: white;
            -fx-selection-bar: #2979ff;
            -fx-selection-bar-text: white;
            -fx-table-cell-border-color: #2c2c2c;
            -fx-border-color: #3a3a3a;
            -fx-font-size: 13px;
        """);

        Scene escena = new Scene(layout, 720, 400);
        ventana.setScene(escena);
        ventana.show();
    }

    // ✅ Método público para obtener chistes desde una base de datos
    public static List<Chiste> obtenerChistes(Database database, String query) {
        List<Chiste> chistes = new ArrayList<>();

        try (Connection conn = database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                chistes.add(new Chiste(
                        rs.getInt("AUTOID"),
                        rs.getString("chiste_en_ingles"),
                        rs.getString("chiste_en_espanol")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chistes;
    }
}
