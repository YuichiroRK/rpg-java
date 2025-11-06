package database;

import javafx.beans.property.*;

public class Chistes {

    private final IntegerProperty id;
    private final StringProperty english;
    private final StringProperty spanish;

    public Chistes(int id, String english, String spanish) {
        this.id = new SimpleIntegerProperty(id);
        this.english = new SimpleStringProperty(english);
        this.spanish = new SimpleStringProperty(spanish);
    }

    // === Getters ===
    public int getId() {
        return id.get();
    }

    public String getEnglish() {
        return english.get();
    }

    public String getSpanish() {
        return spanish.get();
    }

    // === Setters ===
    public void setId(int id) {
        this.id.set(id);
    }

    public void setEnglish(String english) {
        this.english.set(english);
    }

    public void setSpanish(String spanish) {
        this.spanish.set(spanish);
    }

    // === Properties ===
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty englishProperty() {
        return english;
    }

    public StringProperty spanishProperty() {
        return spanish;
    }

    // === Compatibilidad con nombres antiguos ===
    public String getChisteEnIngles() {
        return getEnglish();
    }

    public String getChisteEnEspanol() {
        return getSpanish();
    }

    public void setChisteEnIngles(String value) {
        setEnglish(value);
    }

    public void setChisteEnEspanol(String value) {
        setSpanish(value);
    }
}
