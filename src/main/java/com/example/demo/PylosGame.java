
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

//                case "e":
//                    gameServer.broadcast("OUTPUT:Game exited, goodbye!");
//                    try {
//                        gameServer.shutdown();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.exit(0);
//                    break;
//
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
        gameServer.broadcast("WINNER:" + currentPlayer.getSymbol());
    }
}
