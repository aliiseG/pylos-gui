package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import org.controlsfx.control.spreadsheet.Grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    @FXML private Circle playerSphereDisplay;
    @FXML private Label playerTurnDisplay;
    @FXML private Label playerSphereCount;
    private ImagePattern spherePattern1;
    private ImagePattern spherePattern2;
    private ImagePattern spherePatternEmpty;

    @FXML
    private void exitProgram(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

//    @FXML
//    private void restartGame(ActionEvent event) {
//        gameInstance = new PylosGame(this);
//        gameInstance.startGame();
//    }

    private PrintWriter out;
    private BufferedReader in;

    public void connectToServer(String host, int port) {
        new Thread(() -> {
            try (Socket socket = new Socket(host, port)) {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                displayRules();
                spherePattern1 = new ImagePattern(new Image(getClass().getResource("/assets/Ball-light.png").toExternalForm()), 0, 0, 1, 1, true);
                spherePattern2= new ImagePattern(new Image(getClass().getResource("/assets/Ball-dark.png").toExternalForm()), 0, 0, 1, 1, true);
                spherePatternEmpty = new ImagePattern(new Image(getClass().getResource("/assets/Ball-default.png").toExternalForm()), 0, 0, 1, 1, true);
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
    public void drawNewBoard(Board board) {

    }
    public void drawBoard(List<String> boardSpaces) {
        // Clear spheres from before
        if (gridLevel1.getChildren() != null || gridLevel2.getChildren() != null || gridLevel3.getChildren() != null || gridLevel4.getChildren() != null) {
            gridLevel1.getChildren().clear();
            gridLevel2.getChildren().clear();
            gridLevel3.getChildren().clear();
            gridLevel4.getChildren().clear();
        }
        // added shadow effect to the spheres
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(-2);
        shadow.setOffsetY(2);
        shadow.setRadius(3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));

        int[] levelSizes = {4, 3, 2, 1};
        int index = 0;

        // create string arr with "empty" spots
        String[] entireBoard = new String[30];
        Arrays.fill(entireBoard, " ");
        // add all info about spaces from server to String array in correct order
        for (String spaces : boardSpaces) {
            String[] parts = spaces.split(":");
            int i = Integer.parseInt(parts[0]);
            String spaceInfo = parts[1];
            entireBoard[i] = spaceInfo;
        }
        GridPane[] levels = {gridLevel1, gridLevel2, gridLevel3, gridLevel4};

        // always have the bottom layer printed - default or with the spheres
        for (int row = 0; row < levelSizes[0]; row++) {
            GridPane level = levels[0];
            for (int col = 0; col < levelSizes[0]; col++) {
                String symbol = entireBoard[index++];
                Circle circle = new Circle(20);
                circle.setEffect(shadow);
                circle.setFill(
                        switch (symbol) {
                            case "*" -> spherePattern1;
                            case "#" -> spherePattern2;
                            default -> spherePatternEmpty;
                        }
                );
                StackPane centeredCell = new StackPane(circle);
                level.add(centeredCell, col, row);
            }
        }
        // top layers with transparent free spaces
        for (int m = 1; m < 4; m++) {
            int size = levelSizes[m];
            GridPane level = levels[m];

            for (int row = 0; row < levelSizes[m]; row++) {
                for (int col = 0; col < size; col++) {
                    String symbol = entireBoard[index++];
                    if (Objects.equals(symbol, "*") || Objects.equals(symbol, "#") || Objects.equals(symbol, "available")){
                        Circle circle = new Circle(20);
                        circle.setEffect(shadow);
                        switch (symbol) {
                            case "*":
                                circle.setFill(spherePattern1);
                                break;
                            case "#":
                                circle.setFill(spherePattern2);
                                break;
                            default:
                                circle.setFill(spherePatternEmpty);
                                circle.setOpacity(0.5); // Fully transparent initially

                                // Add hover behavior only for empty spaces
                                //circle.setOnMouseEntered(e -> circle.setOpacity(0.5)); // 50% visible on hover
                                //circle.setOnMouseExited(e -> circle.setOpacity(0.0));  // Back to invisible
                                break;
                        }
                        StackPane centeredCell = new StackPane(circle);
                        level.add(centeredCell, col, row);
                    }

                }
            }
        }
    }


    private void handleServerMessage(String msg) {
        if (msg.startsWith("INPUT:")) {
            // Prompt for player input using GUI
            showInputDialog(msg);
        } else if (msg.startsWith("BOARD:")) {
            String boardString = msg.substring("BOARD:".length());
            List<String> boardStringArr = Arrays.asList(boardString.split(","));
            drawBoard(boardStringArr);
        } else if (msg.startsWith("PLAYER SYMBOL:")){
            if (msg.contains("*")){
                playerSphereDisplay.setFill(spherePattern1);
            } else if (msg.contains("#")){
                playerSphereDisplay.setFill(spherePattern2);
            }
        } else if (msg.startsWith("TURN:")){
            playerTurnDisplay.setText(msg.substring("TURN:".length()));
        } else if (msg.startsWith("WAIT TURN:")){
            playerTurnDisplay.setText(msg.substring("WAIT TURN:".length()));
        } else if (msg.startsWith("SPHERES LEFT:")){
            playerSphereCount.setText(msg.substring("SPHERES LEFT:".length()));
        }
        else{
            messageArea.setText(msg + "\n");
        }
    }
    private void displayRules(){
        rulesLabel.setText("Welcome to PYLOS!\n" +
                "\nGoal of the game is to be the player to place the last sphere on top of the pyramid.\n" +
                "\nEach player has fifteen spheres and the pyramid has four levels in total.\n" +
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

