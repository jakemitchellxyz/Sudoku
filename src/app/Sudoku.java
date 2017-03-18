package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
public class Sudoku extends Application {

    @Override
    public void start(Stage window) throws Exception {
        // Give reference variable to change the scene on button presses
        Scenes.init(window);

        // Create Menu Scene
        Scenes.createScene("Sudoku Menu");

        // Create Window
        window.setTitle("Sudoku");
        window.resizableProperty().setValue(false);
        window.setScene(Scenes.getScene("Sudoku Menu"));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
