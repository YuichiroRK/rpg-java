package rpg.ui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import rpg.model.Card;

public class CardView extends VBox {

    public CardView(Card card) {

        setSpacing(10);
        setAlignment(Pos.CENTER);
        setStyle("-fx-padding: 10; -fx-background-color: #2b2b2b; -fx-border-radius: 10;");

        Image image = new Image(getClass().getResourceAsStream(card.getImagePath()));
        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(200);
        imgView.setPreserveRatio(true);

        Text name = new Text(card.getName());
        name.setStyle("-fx-fill: gold; -fx-font-size: 18px;");

        Text rarity = new Text(card.getRarity().toString());
        rarity.setStyle("-fx-fill: white; -fx-font-size: 14px;");

        Text power = new Text("Poder: " + card.getPower());
        power.setStyle("-fx-fill: lightblue; -fx-font-size: 14px;");


        getChildren().addAll(imgView, name, rarity, power);
    }
}
