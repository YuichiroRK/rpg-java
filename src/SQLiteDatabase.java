import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase implements Database {

    private String url;

    public SQLiteDatabase(String url) {
        this.url = url;
    }

    @Override
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Conectado a la base de datos SQLite");
            return conn;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
