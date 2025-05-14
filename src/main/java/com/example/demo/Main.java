//package com.example.demo;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class Main extends Application {
//
//    private final int[] board = new int[16]; // 0 = empty, 1 = player *, 2 = player #
//    private boolean isPlayerOneTurn = true;
//    private Game game;
//
//    @Override
//    public void start(Stage primaryStage) {
//        Pane mainPane = new Pane();
//        drawLevelOne(mainPane);
//
//        Scene scene = new Scene(mainPane, 400, 400);
//        primaryStage.setTitle("Pylos Level 1 Demo");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//    private void updateBoard() {
//
//    }
//    private void drawLevelOne(Pane mainPane) {
//        Game game = new Game();
////        game.start();
//        double radius = 30;
//        double gap = 70;
//        double startX = 90;
//        double startY = 90;
//        List<Space> gameBoardSpaces = game.getGameBoard().boardSpaces;
//
//        for (int row = 0; row < 4; row++) {
//            for (int col = 0; col < 4; col++) {
//                int index = row * 4 + col;
//                double x = startX + col * gap;
//                double y = startY + row * gap;
//
//                Circle circle = new Circle(x, y, radius);
//                if (gameBoardSpaces.get(index).getSpacePlayer() == " "){
//                    circle.setFill(Color.LIGHTGRAY);
//                    circle.setStroke(Color.BLACK);
//                }
//
//
//                int finalIndex = index;
//                circle.setOnMouseClicked(e -> handleClick(circle, finalIndex));
//
//                mainPane.getChildren().add(circle);
//            }
//        }
////        Button button = new Button("Place new");
////        mainPane.getChildren().add(button);
////        Button button2 = new Button("Remove existing");
////        mainPane.getChildren().add(button2);
////        Button button3 = new Button("Restart");
////        mainPane.getChildren().add(button3);
////        Button button4 = new Button("Exit");
////        mainPane.getChildren().add(button4);
//    }
//
//    private void handleClick(Circle circle, int index) {
//        if (board[index] != 0) return; // Already occupied
//
//        if (isPlayerOneTurn) {
//            circle.setFill(Color.CORNFLOWERBLUE); // Player *
//            board[index] = 1;
//        } else {
//            circle.setFill(Color.CRIMSON); // Player #
//            board[index] = 2;
//        }
//
//        isPlayerOneTurn = !isPlayerOneTurn;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
