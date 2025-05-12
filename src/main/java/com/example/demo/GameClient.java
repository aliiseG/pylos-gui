package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get custom game fonts
        Font.loadFont(getClass().getResourceAsStream("/assets/GomePixel-ARjd7.otf"), 36);
        Font.loadFont(getClass().getResourceAsStream("/assets/Poxast-R9Jjl.ttf"), 36);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/pylos-game-icon.png")));
        // Load fxml file for layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pylos-melnraksts.fxml"));

        // Get parent element of layout (mainGridPane)
        Parent mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pylos Game");
        primaryStage.show();

        GameClientController controller = loader.getController();
        controller.connectToServer("localhost", 1234);
        String boardString = "*,#,#,*,*,*,#,*,#,#,#,*,#,*,*,*,#,#,*,*,*,*,#,#,#,*,#,*,#,*,";
        List<String> spaces = Arrays.asList(boardString.split(","));
        controller.drawBoard(spaces);
    }

    public static void main(String[] args) {
        launch(args);
    }
}