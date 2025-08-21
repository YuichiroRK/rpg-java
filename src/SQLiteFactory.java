public class SQLiteFactory extends DatabaseFactory {

    private String url;

    public SQLiteFactory(String filePath) {
        this.url = "jdbc:sqlite:" + filePath;
    }

    @Override
    public Database createDatabase() {
        return new SQLiteDatabase(url);
    }
}
