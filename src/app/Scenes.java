package app;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        // Game Menu
        Menu menuMenu = new Menu("_Menu");

        // Back to Main Menu
        MenuItem mainMenu = new MenuItem("Back to Main Menu");
        mainMenu.setOnAction(e -> Main.window.setScene(Scenes.getScene("MainMenu")));
        menuMenu.getItems().add(mainMenu);

        menuMenu.getItems().add(new SeparatorMenuItem());

        // Exit Menu
        MenuItem exit = new MenuItem("_Exit");
        exit.setOnAction(e -> Platform.exit());
        menuMenu.getItems().add(exit);

        // Game Menu
        Menu gameMenu = new Menu("_Game");

        // Reset Board
        MenuItem resetBoard = new MenuItem("_Reset Board");
        resetBoard.setOnAction(e -> GameLogic.resetPuzzle());
        gameMenu.getItems().add(resetBoard);

        // New Game Menu
        Menu newGame = new Menu("New _Puzzle");

        MenuItem easy = new MenuItem("_Easy");
        easy.setOnAction(e -> {
            GameLogic.createPuzzle(0);
            Scenes.createScene("Game");
            Main.window.setScene(Scenes.getScene("Game"));
        });

        MenuItem medium = new MenuItem("Me_dium");
        medium.setOnAction(e -> {
            GameLogic.createPuzzle(1);
            Scenes.createScene("Game");
            Main.window.setScene(Scenes.getScene("Game"));
        });

        MenuItem hard = new MenuItem("_Hard");
        hard.setOnAction(e -> {
            GameLogic.createPuzzle(2);
            Scenes.createScene("Game");
            Main.window.setScene(Scenes.getScene("Game"));
        });

        newGame.getItems().addAll(easy, medium, hard);
        gameMenu.getItems().add(newGame);

        if (title == "MainMenu") {
            // Disable Options
            mainMenu.setDisable(true);
            resetBoard.setDisable(true);

            // Center Menu
            StackPane centerMenu = new StackPane();
            Label welcomeLabel = new Label("Play Sudoku!");
            centerMenu.getChildren().add(welcomeLabel);

            // Layout
            layout.setCenter(centerMenu);

        } else if (title == "Game") {
            // Enable Options
            mainMenu.setDisable(false);
            resetBoard.setDisable(false);

            // Create Puzzle (500x500)
            GridPane puzzle = new GridPane();
            puzzle.setPadding(new Insets(30, 25, 0, 30));

            // For each row (boxes)
            for (int i = 1; i < 4; i++) {
                // For each column (boxes)
                for (int j = 1; j < 4; j++) {
                    // For each row (squares)
                    for (int y = (i * 3) - 3; y < (i * 3); y++) {
                        // For each column (squares)
                        for (int x = (j * 3) - 3; x < (j * 3); x++) {
                            Button square = new Button(GameLogic.getSquare(x, y));
                            square.setMinSize(50, 50);
                            square.setMaxSize(50, 50);
                            square.getStyleClass().add("square");

                            int bottom = (y == (i * 3) - 1) ? 5 : 0;
                            int right = (x == (j * 3) - 1) ? 5 : 0;

                            GridPane.setMargin(square, new Insets(0, right, bottom, 0));
                            GridPane.setConstraints(square, x, y);
                            puzzle.getChildren().add(square);
                        }
                    }
                }
            }

            // Numbers
            HBox numbers = new HBox();

            for (int i = 1; i < 10; i++) {
                Button num = new Button("" + i);
                num.setMinSize(51, 51);
                num.setMaxSize(51, 51);
                num.getStyleClass().add("numbers");
                numbers.getChildren().add(num);
            }

            GridPane.setMargin(numbers, new Insets(20, 0, 0, 0));
            GridPane.setConstraints(numbers, 0, 9, 9, 1);
            puzzle.getChildren().add(numbers);


            // Controls
            HBox controls = new HBox();

            Button pencil = new Button("Toggle Pencil");
            pencil.setOnAction(e -> GameLogic.togglePencil());
            controls.getChildren().add(pencil);

            Button home = new Button("Return to Home");
            home.setOnAction(e -> Main.window.setScene(Scenes.getScene("MainMenu")));
            controls.getChildren().add(home);

            layout.setCenter(puzzle);
            layout.setBottom(controls);
        }

        // Add Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuMenu, gameMenu);

        layout.setTop(menuBar);

        // Create Scene
        Scene scene = new Scene(layout, 520, 600);
        scene.getStylesheets().add("Styles.css");

        scenes.put(title, scene);
    }
}
