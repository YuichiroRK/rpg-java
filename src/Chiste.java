import javafx.beans.property.*;

public class Chiste {
    private final IntegerProperty id;
    private final StringProperty english;
    private final StringProperty spanish;

    public Chiste(int id, String english, String spanish) {
        this.id = new SimpleIntegerProperty(id);
        this.english = new SimpleStringProperty(english);
        this.spanish = new SimpleStringProperty(spanish);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty englishProperty() { return english; }
    public StringProperty spanishProperty() { return spanish; }
}
