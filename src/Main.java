import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
/*
        // Conexión a SQLite con Factory
        DatabaseFactory sqliteFactory = new SQLiteFactory();
        Database sqliteDb = sqliteFactory.createDatabase("jdbc:sqlite:baseproduccion.db");

        try (Connection conn = sqliteDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY, nombre TEXT)");
            stmt.execute("INSERT OR REPLACE INTO usuarios (id, nombre) VALUES (67001155, 'Alexis')");
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios");

            System.out.println("Datos desde SQLite:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error SQLite: " + e.getMessage());
        }

        // Conexión a MySQL con Factory
        DatabaseFactory mysqlFactory = new MySQLFactory();
        Database mysqlDb = mysqlFactory.createDatabase("jdbc:mysql://172.30.16.192:3306/construccion1");

        try (Connection conn = mysqlDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id INT PRIMARY KEY, nombre VARCHAR(100))");
            stmt.execute("INSERT INTO usuarios (id, nombre) VALUES (67001155, 'Alexis') " +
                    "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre)");

            ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios");

            System.out.println("Datos desde MySQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error MySQL: " + e.getMessage());
        }
*/
        // Conexión a PostgreSQL con Factory
        DatabaseFactory postgresFactory = new PostgresFactory();
        Database postgresDb = postgresFactory.createDatabase("jdbc:postgresql://172.30.16.248:5432/construccion1");

        try (Connection conn = postgresDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (id SERIAL PRIMARY KEY, nombre VARCHAR(100))");
            stmt.execute("INSERT INTO usuarios (id, nombre) VALUES (67001153, 'Chabes') " +
                    "ON CONFLICT (id) DO UPDATE SET nombre='Andres'");
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios");

            System.out.println("Datos desde PostgreSQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error PostgreSQL: " + e.getMessage());
     
        }
        try {
            // Usamos ChuckNorrisAdapter para obtener el chiste y traducirlo
            JokeService jokeService = new ChuckNorrisAdapter();
            String joke = jokeService.getJoke();  // Obtiene el chiste tanto en inglés como en español

            // Imprime el chiste en ambos idiomas
            System.out.println(joke);

        } catch (Exception e) {
            System.out.println("Error al obtener o traducir el chiste: " + e.getMessage());
        }
    }
}
