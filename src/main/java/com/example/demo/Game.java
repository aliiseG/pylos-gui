package com.example.demo;

public abstract class Game {
    protected Board gameBoard;
    protected Player player1;
    protected Player player2;
    protected Player currentPlayer;

    public Game() {
        gameBoard = new Board();
        player1 = new Player("*");
        player2 = new Player("#");
        currentPlayer = player1;
    }

    public final void startGame() {
        while (gameBoard.checkWinner().equals(" ")) {
            broadcastGameState();

            showPlayerSphereCounts();

            notifyTurn(currentPlayer, getOtherPlayer());

            Move move = getNextMove();

            if (move != null) {
                move.makeMove(gameBoard);
                postMoveFeedback();
                switchTurn();
            }
        }

        announceWinner();
    }

    protected void switchTurn() {
        Player otherPlayer = getOtherPlayer();
        if (otherPlayer.hasSpheres()) {
            currentPlayer = otherPlayer;
        }
    }

    protected Player getOtherPlayer() {
        return (currentPlayer == player1) ? player2 : player1;
    }

    protected abstract Move getNextMove();
    protected abstract void broadcastGameState();
    protected abstract void showPlayerSphereCounts();
    protected abstract void notifyTurn(Player current, Player other);
    protected abstract void postMoveFeedback();
    protected abstract void announceWinner();
}
