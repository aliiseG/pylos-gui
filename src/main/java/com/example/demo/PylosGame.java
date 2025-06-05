//package com.example.demo;
//
//import java.io.IOException;
//import java.util.InputMismatchException;
//
//
//public class PylosGame {
//    private Board gameBoard;
//    private Player player1;
//    private Player player2;
//    public Player currentPlayer;
//    private final GameServer gameServer;
//
//    public PylosGame(GameServer gameServer) {
//        gameBoard = new Board();
//        player1 = new Player("*");
//        player2 = new Player("#");
//        currentPlayer = player1;
//        this.gameServer = gameServer;
//    }
//
//    public void startGame() {
//        while (gameBoard.checkWinner().equals(" ")) {
//            // send both players board status to draw spheres on boards
//            gameServer.broadcast("BOARD:" + gameBoard.getCurrentGameboard());
//
//            gameServer.sendToPlayer(currentPlayer, "SPHERES LEFT:"+currentPlayer.getSphereCount());
//            gameServer.sendToPlayer(currentPlayer == player1 ? player2 : player1, "SPHERES LEFT:"+(currentPlayer == player1 ? player2 : player1).getSphereCount());
//            // output to players whose turn it is
//            gameServer.notifyPlayersTurn(currentPlayer, currentPlayer == player1 ? player2 : player1);
//
//            Move move = getMoveFromServer();
//            if (move != null) {
//                move.makeMove(gameBoard);
//                gameServer.sendToPlayer(currentPlayer, "OUTPUT: ");
//                switchTurn();
//            }
//        }
//        System.out.println("Winner is: " + currentPlayer.getSymbol());
//    }
//
//    private void switchTurn() {
//        // switches to other player only if they have spheres left to use
//        Player otherPlayer = (currentPlayer == player1) ? player2 : player1;
//        if (otherPlayer.hasSpheres()) {
//            currentPlayer = otherPlayer;
//        }
//    }
//
//    // get move as string through server from player, check and return as Move object
//    private Move getMoveFromServer() {
//        Move move = null;
//        // Send to client (who is current player) request for input
//        String playersMoveInput = gameServer.sendToPlayer(currentPlayer, "INPUT: Select move type: place new (place) + space / remove existing (remove) + space. Or exit game (e) / restart (re)");
//        int spaceInput;
//        int spaceInput2;
//        try {
//            String[] inputParts = playersMoveInput.split(" ");
//            if (!inputParts[0].equals("place") &&
//                    !inputParts[0].equals("remove") &&
//                    !inputParts[0].equals("e") &&
//                    !inputParts[0].equals("re")) {
//                gameServer.sendToPlayer(currentPlayer,
//                        "OUTPUT:Invalid input!!!!!");
//            } else {
//                switch (inputParts[0]) {
//                    case ("place"):
//                        spaceInput = Integer.parseInt(inputParts[1]);
//                        if (spaceInput >= 0 && gameBoard.getFreeSpots().contains(spaceInput)) {
//                            move = new PlaceNew(spaceInput, currentPlayer);
//                        } else {
//                            gameServer.sendToPlayer(currentPlayer,
//                                    "OUTPUT:Select different space!");
//                            break;
//                        }
//                        break;
//                    case ("remove"):
//                        if (gameBoard.checkRemoveRule(currentPlayer)) {
//                            int amountToRemove = inputParts.length;
//                            boolean validMove = false;
//
//                            while (!validMove) {
//                                if (amountToRemove == 2) {
//                                    spaceInput = Integer.parseInt(inputParts[1]);
//                                    if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer)) {
//                                        move = new RemoveExisting(spaceInput, 42, currentPlayer); // 42 as dummy
//                                        validMove = true;
//                                    } else {
//                                        gameServer.sendToPlayer(currentPlayer, "OUTPUT:Invalid space. Try again.");
//                                        break;
//                                    }
//
//                                } else if (amountToRemove == 3) {
//                                    spaceInput = Integer.parseInt(inputParts[1]);
//                                    spaceInput2 = Integer.parseInt(inputParts[2]);
//
//                                    if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer) &&
//                                            gameBoard.checkRemoveSphere(spaceInput2, currentPlayer)) {
//                                        move = new RemoveExisting(spaceInput, spaceInput2, currentPlayer);
//                                        validMove = true;
//                                    } else {
//                                        gameServer.sendToPlayer(currentPlayer, "OUTPUT:One or both spaces are invalid. Try again.");
//                                        break;
//                                    }
//
//                                } else {
//                                    gameServer.sendToPlayer(currentPlayer, "OUTPUT:You can only remove 1 or 2 spheres.");
//                                    break;
//                                }
//
//                            }
//
//                        } else {
//                            gameServer.sendToPlayer(currentPlayer,
//                                    "OUTPUT:You have no spheres to remove!");
//                            break;
//                        }
//                        break;
//                    case ("e"):
//                        gameServer.broadcast("OUTPUT:Game exited, goodbye!");
//                        try {
//                            gameServer.shutdown();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        System.exit(0);
//                        break;
//                    case ("re"):
//                        gameBoard = new Board();
//                        player1 = new Player("*");
//                        player2 = new Player("#");
//                        currentPlayer = player1;
//                        startGame();
//                        break;
//                }
//            }
//        } catch (InputMismatchException e) {
//            System.out.println(
//                    "OUTPUT:Invalid input!!");
//            move = getMoveFromServer();
//        }
//        return move;
//    }
//}
package com.example.demo;

import java.io.IOException;
import java.util.InputMismatchException;

public class PylosGame extends Game {
    private final GameServer gameServer;

    public PylosGame(GameServer gameServer) {
        super(); // initialize gameBoard, players, currentPlayer
        this.gameServer = gameServer;
    }

    @Override
    protected Move getNextMove() {
        Move move = null;
        String input = gameServer.sendToPlayer(currentPlayer,
                "INPUT: Select move type: place new (place) + space / remove existing (remove) + space. Or exit game (e) / restart (re)");
        if (input == null || input.isEmpty()) return null;

        try {
            String[] parts = input.trim().split(" ");
            String command = parts[0];

            switch (command) {
                case "place":
                    int space = Integer.parseInt(parts[1]);
                    if (gameBoard.getFreeSpots().contains(space)) {
                        move = new PlaceNew(space, currentPlayer);
                    } else {
                        gameServer.sendToPlayer(currentPlayer, "OUTPUT:Select different space!");
                    }
                    break;

                case "remove":
                    if (gameBoard.checkRemoveRule(currentPlayer)) {
                        if (parts.length == 2) {
                            int s1 = Integer.parseInt(parts[1]);
                            if (gameBoard.checkRemoveSphere(s1, currentPlayer)) {
                                move = new RemoveExisting(s1, 42, currentPlayer); // dummy 42
                            } else {
                                gameServer.sendToPlayer(currentPlayer, "OUTPUT:Invalid space. Try again.");
                            }
                        } else if (parts.length == 3) {
                            int s1 = Integer.parseInt(parts[1]);
                            int s2 = Integer.parseInt(parts[2]);
                            if (gameBoard.checkRemoveSphere(s1, currentPlayer) &&
                                    gameBoard.checkRemoveSphere(s2, currentPlayer)) {
                                move = new RemoveExisting(s1, s2, currentPlayer);
                            } else {
                                gameServer.sendToPlayer(currentPlayer, "OUTPUT:One or both spaces are invalid.");
                            }
                        } else {
                            gameServer.sendToPlayer(currentPlayer, "OUTPUT:You can only remove 1 or 2 spheres.");
                        }
                    } else {
                        gameServer.sendToPlayer(currentPlayer, "OUTPUT:You have no spheres to remove!");
                    }
                    break;

                case "e":
                    gameServer.broadcast("OUTPUT:Game exited, goodbye!");
                    try {
                        gameServer.shutdown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                    break;

                case "re":
                    gameBoard = new Board();
                    player1 = new Player("*");
                    player2 = new Player("#");
                    currentPlayer = player1;
                    startGame();
                    break;

                default:
                    gameServer.sendToPlayer(currentPlayer, "OUTPUT:Invalid input!!!!!");
            }

        } catch (InputMismatchException | NumberFormatException e) {
            gameServer.sendToPlayer(currentPlayer, "OUTPUT:Invalid input format!");
            return getNextMove();
        }

        return move;
    }

    @Override
    protected void broadcastGameState() {
        gameServer.broadcast("BOARD:" + gameBoard.getCurrentGameboard());
    }

    @Override
    protected void showPlayerSphereCounts() {
        gameServer.sendToPlayer(currentPlayer, "SPHERES LEFT:" + currentPlayer.getSphereCount());
        gameServer.sendToPlayer(getOtherPlayer(), "SPHERES LEFT:" + getOtherPlayer().getSphereCount());
    }

    @Override
    protected void notifyTurn(Player current, Player other) {
        gameServer.notifyPlayersTurn(current, other);
    }

    @Override
    protected void postMoveFeedback() {
        gameServer.sendToPlayer(currentPlayer, "OUTPUT:");
    }

    @Override
    protected void announceWinner() {
        gameServer.broadcast("OUTPUT:Winner is: " + currentPlayer.getSymbol());
    }
}
