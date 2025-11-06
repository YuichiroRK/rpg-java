package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;


public class Main {
    public static void main(String[] args) {

        // Obtener el chiste de Chuck Norris
        String jokeInEnglish = "";
        String jokeInSpanish = "";
        try {
            // NOTA: Aquí no se crea todavía ChuckNorrisAdapter porque requiere la base de datos
            // Se hará después, tras crear la conexión MySQL
        } catch (Exception e) {
            System.out.println("Error al obtener o traducir el chiste: " + e.getMessage());
            return;
        }

        // Conexión a SQLite con Factory
        DatabaseFactory sqliteFactory = new SQLiteFactory();
        Database sqliteDb = sqliteFactory.createDatabase("jdbc:sqlite:baseproduccion.db");

        try (Connection conn = sqliteDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS ChistesChabes (" +
                    "AUTOID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "chiste_en_ingles TEXT, " +
                    "chiste_en_espanol TEXT)");

        } catch (SQLException e) {
            System.out.println("Error SQLite: " + e.getMessage());
        }

        // Conexión a MySQL con Factory
        DatabaseFactory mysqlFactory = new MySQLFactory();
        Database mysqlDb = mysqlFactory.createDatabase("jdbc:mysql://172.30.16.186:3306/construccion1");

        try (Connection conn = mysqlDb.connect();
             Statement stmt = conn.createStatement()) {

            // Mostrar tablas en la base de datos MySQL
            ResultSet rsTables = stmt.executeQuery("SHOW TABLES");
            System.out.println("Tablas en MySQL (construccion1):");
            while (rsTables.next()) {
                System.out.println(" - " + rsTables.getString(1));
            }

            // Mostrar columnas de la tabla 'usuarios'
            System.out.println("\nColumnas en la tabla 'usuarios':");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rsColumns = metaData.getColumns(null, null, "usuarios", null);

            while (rsColumns.next()) {
                String columnName = rsColumns.getString("COLUMN_NAME");
                String dataType = rsColumns.getString("TYPE_NAME");
                int columnSize = rsColumns.getInt("COLUMN_SIZE");
                String nullable = rsColumns.getString("IS_NULLABLE");
                String autoIncrement = rsColumns.getString("IS_AUTOINCREMENT");

                System.out.printf(" - %s (%s), tamaño: %d, Nullable: %s, AutoIncrement: %s%n",
                        columnName, dataType, columnSize, nullable, autoIncrement);
            }

        } catch (SQLException e) {
            System.out.println("Error MySQL: " + e.getMessage());
        }


        // Conexión a PostgreSQL con Factory
        DatabaseFactory postgresFactory = new PostgresFactory();
        Database postgresDb = postgresFactory.createDatabase("jdbc:postgresql://172.30.16.187:5432/construccion1");

        try (Connection conn = postgresDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS ChistesChabes (" +
                    "AUTOID SERIAL PRIMARY KEY, " +
                    "chiste_en_ingles TEXT, " +
                    "chiste_en_espanol TEXT)");

            // Insertar el chiste obtenido de Chuck Norris
            stmt.execute("INSERT INTO ChistesChabes (chiste_en_ingles, chiste_en_espanol) " +
                    "VALUES ('" + jokeInEnglish + "', '" + jokeInSpanish + "') " +
                    "ON CONFLICT (AUTOID) DO UPDATE SET chiste_en_ingles = EXCLUDED.chiste_en_ingles, chiste_en_espanol = EXCLUDED.chiste_en_espanol");

            ResultSet rs = stmt.executeQuery("SELECT * FROM ChistesChabes");


            System.out.println("Datos desde PostgreSQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("AUTOID") + " - Inglés: " + rs.getString("chiste_en_ingles") +
                        " | Español: " + rs.getString("chiste_en_espanol"));
            }

        } catch (SQLException e) {
            System.out.println("Error PostgreSQL: " + e.getMessage());
        }
    }

    private static String escapeSingleQuotes(String str) {
        if (str != null) {
            return str.replace("'", "''");
        }
        return str;
    }
}
