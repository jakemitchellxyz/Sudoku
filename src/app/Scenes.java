package app;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
class Scenes {
    private static HashMap<String, Scene> scenes = new HashMap<>();

    static Scene getScene(String title) {
        return scenes.get(title);
    }

    static void createScene(String title) {
        BorderPane layout = new BorderPane();

        if (title == "MainMenu") {
            // Top Menu
            HBox topMenu = new HBox();
            Button fileBtn = new Button("File");
            Button editBtn = new Button("Edit");
            Button viewBtn = new Button("View");
            topMenu.getChildren().addAll(fileBtn, editBtn, viewBtn);

            // Left Menu
            VBox leftMenu = new VBox();
            Button easyBtn = new Button("Easy");
            easyBtn.setOnAction(e -> {
                GameLogic.createPuzzle(0);
                Scenes.createScene("Game");
                Main.window.setScene(Scenes.getScene("Game"));
            });
            Button mediumBtn = new Button("Medium");
            mediumBtn.setOnAction(e -> {
                GameLogic.createPuzzle(1);
                Scenes.createScene("Game");
                Main.window.setScene(Scenes.getScene("Game"));
            });
            Button hardBtn = new Button("Hard");
            hardBtn.setOnAction(e -> {
                GameLogic.createPuzzle(2);
                Scenes.createScene("Game");
                Main.window.setScene(Scenes.getScene("Game"));
            });
            leftMenu.getChildren().addAll(easyBtn, mediumBtn, hardBtn);

            // Center Menu
            StackPane centerMenu = new StackPane();
            Label welcomeLabel = new Label("Play Sudoku!");
            centerMenu.getChildren().add(welcomeLabel);

            // Layout
            layout.setTop(topMenu);
            layout.setLeft(leftMenu);
            layout.setCenter(centerMenu);

        } else if (title == "Game") {
            // Create Puzzle
            GridPane puzzle = new GridPane();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Button button = new Button(GameLogic.getSquare(j, i));
                    button.setMinWidth(50);
                    button.setMaxWidth(50);
                    button.setMinHeight(50);
                    button.setMaxHeight(50);
                    GridPane.setConstraints(button, i, j);
                    puzzle.getChildren().add(button);
                }
            }

            // Controls
            HBox controls = new HBox();
            Button pencil = new Button("Toggle Pencil");
            pencil.setOnAction(e -> GameLogic.togglePencil());
            Button home = new Button("Return to Home");
            home.setOnAction(e -> Main.window.setScene(Scenes.getScene("MainMenu")));
            controls.getChildren().add(pencil);
            controls.getChildren().add(home);

            layout.setCenter(puzzle);
            layout.setBottom(controls);
        }

        // Create Scene
        Scene scene = new Scene(layout, 500, 600);

        scenes.put(title, scene);
    }
}
