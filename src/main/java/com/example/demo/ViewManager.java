package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewManager {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    // initialize viewmanager
    public ViewManager(){
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();

        mainStage.setScene(mainScene);
        createButtons();
    }

    public Stage getMainStage(){
        return mainStage;
    }

    public void createButtons(){
        Button button = new Button();
        mainPane.getChildren().add(button);
    }

}
