package com.example.demo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class PylosGame {
    private Board gameBoard;
    private Player player1;
    private Player player2;
    public Player currentPlayer;
    private Scanner scanner;
    private GameServer gameServer;

    public PylosGame(GameServer gameServer) {
        gameBoard = new Board();
        player1 = new Player("*");
        player2 = new Player("#");
        currentPlayer = player1;
        scanner = new Scanner(System.in);
        this.gameServer = gameServer;
    }

    public void startGame() {
        printGameRules();
        while (gameBoard.checkWinner().equals(" ")) {
            // nosuta visiem speletajiem tekoso speles lauku
            gameServer.broadcast(gameBoard.getBoardString());
            gameServer.broadcast("Current player: " + currentPlayer.getSymbol());
            // Move move = null;
            // while (move==null){
            //     move = getMoveFromServer();
            // }

            Move move = getMoveFromServer();
            if (move != null) {
                move.makeMove(gameBoard);
                gameServer.broadcast("Player " + currentPlayer.getSymbol() + " after this move has: " + currentPlayer.spheres);
                switchTurn();
            }
        }
        System.out.println("Winner is: " + currentPlayer.getSymbol());
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    // get move as string through server from player, check and return as Move object
    private Move getMoveFromServer() {
        Move move = null;
        String moveInput;
        String playersMoveInput = gameServer.sendToPlayer(currentPlayer, "INPUT: Select move type: place new (p) + space / remove existing (r) + space / move existing (m) + one or two spaces.  Or exit game (e) / restart (re)");
        if (playersMoveInput != null) {
            gameServer.broadcast(playersMoveInput);
        } else gameServer.broadcast("kkas nav");
        int spaceInput;
        int spaceInput2;
        int moveFromSpaceInput;
        try {
            String[] inputParts = playersMoveInput.split(" ");
            if (!inputParts[0].equals("place") &&
                    !inputParts[0].equals("remove") &&
                    !inputParts[0].equals("move") &&
                    !inputParts[0].equals("e") &&
                    !inputParts[0].equals("re")) {
                gameServer.sendToPlayer(currentPlayer,
                        "Invalid input!!!!!");
                // move = getMoveFromServer();
                //return null;
            } else {
                switch (inputParts[0]) {
                    case ("place"):
                        spaceInput = Integer.parseInt(inputParts[1]);
                        if (spaceInput >= 0 && gameBoard.freeSpots.contains(spaceInput)) {
                            move = new PlaceNew(spaceInput, currentPlayer);
                        } else {
                            gameServer.sendToPlayer(currentPlayer,
                                    "Select different space!");
                            break;
                        }
                        break;
                    case ("remove"):
                        if (gameBoard.checkRemoveRule(currentPlayer)) {
                            System.out.println(gameBoard.checkRemoveRule(currentPlayer));
                            int amountToRemove = inputParts.length;
                            boolean validMove = false;

                            while (!validMove) {
                                if (amountToRemove == 1) {
                                    spaceInput = Integer.parseInt(inputParts[1]);
                                    if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer)) {
                                        move = new RemoveExisting(spaceInput, 42, currentPlayer); // 42 as dummy
                                        validMove = true;
                                    } else {
                                        gameServer.sendToPlayer(currentPlayer, "Invalid space. Try again.");
                                    }

                                } else if (amountToRemove == 2) {
                                    spaceInput = Integer.parseInt(inputParts[1]);
                                    spaceInput2 = Integer.parseInt(inputParts[2]);

                                    if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer) &&
                                            gameBoard.checkRemoveSphere(spaceInput2, currentPlayer)) {
                                        move = new RemoveExisting(spaceInput, spaceInput2, currentPlayer);
                                        validMove = true;
                                    } else {
                                        gameServer.sendToPlayer(currentPlayer, "One or both spaces are invalid. Try again.");
                                    }

                                } else {
                                    gameServer.sendToPlayer(currentPlayer, "You can only remove 1 or 2 spheres.");
                                }

                            }

                        } else {
                            gameServer.sendToPlayer(currentPlayer,
                                    "You have no spheres to remove!");
                            break;
                        }
                        break;
                    case ("move"):
                        break;
                    case ("e"):
                        gameServer.broadcast("Game exited, goodbye!");
                        try {
                            gameServer.shutdown();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                        break;
                    case ("re"):
                        gameBoard = new Board();
                        player1 = new Player("*");
                        player2 = new Player("#");
                        currentPlayer = player1;
                        startGame();
                        break;
                }
            }
            ;
        } catch (InputMismatchException e) {
            System.out.println(
                    "Invalid input!!");
            move = getMoveFromServer();
        }
        ;
        return move;
    }

    ;

    public void printGameRules() {
        gameServer.broadcast("\nWelcome to PYLOS!\n" +
                "\nGoal of the game is to be the player to place the last sphere on top of the pyramid.\n" +
                "Each player has 15 spheres and the pyramid has 4 levels in total, marked with numbers 0-29.\n" +

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
                "   |-----|\n" +

                "\nPlayer * starts!\n");
    }
}
