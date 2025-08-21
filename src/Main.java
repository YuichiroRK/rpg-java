public class Main {
    public static void main(String[] args) {
        // Ruta de la base de datos SQLite
        SQLiteFactory factory = new SQLiteFactory("test.db");

        // Crear la base de datos
        Database db = factory.createDatabase();

        // Conectar a la base de datos
        db.connect();
    }
}