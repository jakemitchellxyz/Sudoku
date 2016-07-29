package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
public class Main extends Application {
    static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        // Create Menu Scene
        Scenes.createScene("MainMenu");

        // Create Window
        window.setTitle("Sudoku");
        window.resizableProperty().setValue(false);
        window.setScene(Scenes.getScene("MainMenu"));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
