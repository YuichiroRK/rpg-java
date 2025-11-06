package rpg.model;

public enum Rarity {
    COMMON(50, "⚪", "#CCCCCC"),
    UNCOMMON(25, "🟩", "#66FF66"),
    RARE(12, "🟦", "#3399FF"),
    EPIC(6, "🟪", "#9933FF"),
    LEGENDARY(3, "🟨", "#FFD700"),
    MYTHIC(2, "🟥", "#FF4444"),
    ANCIENT(1, "🟧", "#FF9933"),
    DIVINE(0.5, "🌈", "#FFFFFF"),
    SECRET(0.3, "💠", "#00FFFF"),
    ULTIMATE(0.2, "🔥", "#FF0000");

    private final double weight;
    private final String symbol;
    private final String colorHex;

    Rarity(double weight, String symbol, String colorHex) {
        this.weight = weight;
        this.symbol = symbol;
        this.colorHex = colorHex;
    }

    public double getWeight() {
        return weight;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColorHex() {
        return colorHex;
    }
}
