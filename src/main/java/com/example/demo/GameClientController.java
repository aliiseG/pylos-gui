package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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
    @FXML private Text playerSphereCount;
    private ImagePattern spherePattern1;
    private ImagePattern spherePattern2;
    private ImagePattern spherePatternEmpty;
//    private Boolean startInputPrompt = false;

    @FXML
    private void exitProgram(ActionEvent event) {
        // out.println("e");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void restartGame(ActionEvent event) {
        out.println("re");
    }

    @FXML
    private void startMove(ActionEvent event) {
        if (playerTurnDisplay.getText().contains("Your turn")) {
            showInputDialog("Select move type: place new (place) + space / remove existing (remove) + space. Or exit game (e) / restart (re)");
        } else handleServerMessage("OUTPUT: Wait for your turn!");
    }

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

    public void drawBoard(List<String> boardSpaces) {
        // clear spheres from before (useful when restarting game)
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
                                circle.setOpacity(0.5);
                                break;
                        }
                        StackPane centeredCell = new StackPane(circle);
                        level.add(centeredCell, col, row);
                    }

                }
            }
        }
    }

    // decide what happens after getting message from server
    private void handleServerMessage(String msg) {
        // prompt for player input calling a popup window
//        if (msg.startsWith("INPUT:")) {
//            if (startInputPrompt){
//                showInputDialog(msg);
//            }
//        }
        // get current board state and draw it on players board
        if (msg.startsWith("BOARD:")) {
            String boardString = msg.substring("BOARD:".length());
            List<String> boardStringArr = Arrays.asList(boardString.split(","));
            drawBoard(boardStringArr);
        }
        // display to each player their sphere colors
        else if (msg.startsWith("PLAYER SYMBOL:")){
            if (msg.contains("*")){
                playerSphereDisplay.setFill(spherePattern1);
            } else if (msg.contains("#")){
                playerSphereDisplay.setFill(spherePattern2);
            }
        }
        // display message that its the players turn to make a move
        else if (msg.startsWith("TURN:")){
            playerTurnDisplay.setText(msg.substring("TURN:".length()));
        }
        // display message that its the other players turn to make a move
        else if (msg.startsWith("WAIT TURN:")){
            playerTurnDisplay.setText(msg.substring("WAIT TURN:".length()));
        }
        // display for each player how many spheres they have left
        else if (msg.startsWith("SPHERES LEFT:")){
            playerSphereCount.setText(msg.substring("SPHERES LEFT:".length()));
        }
        // display a message under board from server - errors, invalid move, etc.
        else if (msg.startsWith("OUTPUT")){
            messageArea.setText(msg.substring("OUTPUT:".length()));
        }
        // create dialog for winnner output
        else if (msg.startsWith("WINNER")){
            showWinnerDialog(msg.substring("WINNER:".length()));
        }
//        else{
//            messageArea.setText(msg + "\n");
//        }
    }
    private void displayRules(){
        rulesLabel.setText("Welcome to PYLOS!\n" +
                "\nGoal of the game is to be the player to place the last sphere on top of the pyramid.\n" +
                "\nEach player has fifteen spheres and the pyramid has four levels in total.\n" +
                "\nSelect type of move (place new or remove) and then select the space you wish to use.\n" +
                "\nYou can remove one or two spheres if you make a 2x2 square of your color.\n" +
                "\nSpaces on level one:\n\n" +
                "   |-----|-----|-----|-----|\n" +
                "   |  0  |  1  |  2  |  3  |\n" +
                "   |-----|-----|-----|-----|\n" +
                "   |  4  |  5  |  6  |  7  |\n" +
                "   |-----|-----|-----|-----|\n" +
                "   |  8  |  9  | 10  | 11  |\n" +
                "   |-----|-----|-----|-----|\n" +
                "   | 12  | 13  | 14  | 15  |\n" +
                "   |-----|-----|-----|-----|\n" +

                "\nSpaces on level two:\n\n" +
                "   |-----|-----|-----|\n" +
                "   | 16  | 17  | 18  |\n" +
                "   |-----|-----|-----|\n" +
                "   | 19  | 20  | 21  |\n" +
                "   |-----|-----|-----|\n" +
                "   | 22  | 23  | 24  |\n" +
                "   |-----|-----|-----|\n" +

                "\nSpaces on level three:\n\n" +
                "   |-----|-----|\n" +
                "   | 25  | 26  |\n" +
                "   |-----|-----|\n" +
                "   | 27  | 28  |\n" +
                "   |-----|-----|\n" +

                "\nSpace on level four:\n\n" +
                "   |-----|\n" +
                "   | 29  |\n" +
                "   |-----|\n\n"+
                "\nPress the PLACE NEW button when it's your turn!\n\n");
    }

    // popup window that asks player for move
    private void showInputDialog(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Your Move");
        dialog.setHeaderText(null);
        dialog.setContentText(prompt.replace("INPUT:", ""));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(move -> out.println(move));
    }

    private void showWinnerDialog(String winnerInfo) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Winner");
        dialog.setHeaderText(null);
        dialog.setContentText("Winner is " + winnerInfo +"\n Thank you for playing!");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
    }
}

