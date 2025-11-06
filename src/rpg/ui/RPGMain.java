package rpg.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RPGMain extends Application {
    @Override
    public void start(Stage stage) {
        RNGController controller = new RNGController();
        Scene scene = new Scene(controller.createView(), 400, 350);
        stage.setScene(scene);
        stage.setTitle("RPG - Sistema de Cartas");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
