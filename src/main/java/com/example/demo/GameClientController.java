package com.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class GameClientController {
    @FXML
    private GridPane boardGrid;
    @FXML private Label messageArea;
    @FXML
    private Label rulesLabel;
    @FXML private GridPane gridLevel1;
    @FXML private GridPane gridLevel2;
    @FXML private GridPane gridLevel3;
    @FXML private GridPane gridLevel4;

    private PrintWriter out;
    private BufferedReader in;

    public void connectToServer(String host, int port) {
        new Thread(() -> {
            try (Socket socket = new Socket(host, port)) {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                displayRules();
                String serverMsg;
                while ((serverMsg = in.readLine()) != null) {
                    final String msg = serverMsg;
                    Platform.runLater(() -> handleServerMessage(msg));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void drawBoard(List<String> boardSpaces) {
        // Clear old spheres
//        if (gridLevel1.getChildren() != null || gridLevel2.getChildren() != null || gridLevel3.getChildren() != null || gridLevel4.getChildren() != null) {
//            gridLevel1.getChildren().clear();
//            gridLevel2.getChildren().clear();
//            gridLevel3.getChildren().clear();
//            gridLevel4.getChildren().clear();
//        }
        Image lightSphere = new Image(getClass().getResource("/assets/Ball-light.png").toExternalForm());
        Image darkSphere = new Image(getClass().getResource("/assets/Ball-dark.png").toExternalForm());// Level sizes
        Image defaultSphere = new Image(getClass().getResource("/assets/Ball-default.png").toExternalForm());// Level sizes

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(-2); // Less shift to the left
        shadow.setOffsetY(2);  // Less shift downward
        shadow.setRadius(3);   // Smaller blur radius
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));

        int[] sizes = {4, 3, 2, 1};
        int index = 0;

        GridPane[] levels = {gridLevel1, gridLevel2, gridLevel3, gridLevel4};

        for (int l = 0; l < 4; l++) {
            int size = sizes[l];
            GridPane level = levels[l];

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    String symbol = boardSpaces.get(index++);
                    Circle circle = new Circle(20);
                    circle.setEffect(shadow);
                    circle.setFill(
                            switch (symbol) {
                                case "*" -> new ImagePattern(lightSphere, 0, 0, 1, 1, true);
                                case "#" -> new ImagePattern(darkSphere, 0, 0, 1, 1, true);
                                default -> new ImagePattern(defaultSphere, 0, 0, 1, 1, true);
                            }
                    );
                    StackPane centeredCell = new StackPane(circle);
                    level.add(centeredCell, col, row);
                }
            }
        }
    }


    private void handleServerMessage(String msg) {
        if (msg.startsWith("INPUT:")) {
            // Prompt for player input using GUI
            showInputDialog(msg);
        } else {
            messageArea.setText(msg + "\n");
        }
    }
    private void displayRules(){
        rulesLabel.setText("Welcome to PYLOS!\n" +
                "\nGoal of the game is to be the player to place the last sphere on top of the pyramid.\n" +
                "\nEach player has 15 spheres and the pyramid has 4 levels in total.\n" +
                "\nSelect type of move (place new or remove) and then select the space you wish to use.\n");
    }

    private void showInputDialog(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Your Move");
        dialog.setHeaderText(null);
        dialog.setContentText(prompt.replace("INPUT:", ""));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(move -> out.println(move));
    }
}

