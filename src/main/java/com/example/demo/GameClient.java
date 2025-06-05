package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Objects;

public class GameClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get custom game fonts & icon for window
        Font.loadFont(getClass().getResourceAsStream("/assets/GomePixelRounded-Regular.otf"), 36);
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}