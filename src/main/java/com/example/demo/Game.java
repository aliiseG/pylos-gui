package com.example.demo;




import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private Board gameBoard;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Scanner scanner;

    public Game() {
        gameBoard = new Board();
        player1 = new Player("*");
        player2 = new Player("#");
        currentPlayer = player1;
        scanner = new Scanner(System.in);
    }
    public Board getGameBoard(){
        return gameBoard;
    }

    public void printGameRules(){
        System.out.println("\nWelcome to PYLOS!");
        System.out.println("\nGoal of the game is to be the player to place the last sphere on top of the pyramid.");
        System.out.println("\nEach player has 15 spheres and the pyramid has 4 levels in total, marked with numbers 0-29.");

        System.out.println("\nSpaces on level one:\n");
        System.out.println("   |-----|-----|-----|-----|");
        System.out.println("   |  0  |  1  |  2  |  3  |");
        System.out.println("   |-----|-----|-----|-----|");
        System.out.println("   |  4  |  5  |  6  |  7  |");
        System.out.println("   |-----|-----|-----|-----|");
        System.out.println("   |  8  |  9  | 10  | 11  |");
        System.out.println("   |-----|-----|-----|-----|");
        System.out.println("   | 12  | 13  | 14  | 15  |");
        System.out.println("   |-----|-----|-----|-----|");
        System.out.println("\nSpaces on level two:\n");
        System.out.println("   |-----|-----|-----|");
        System.out.println("   | 16  | 17  | 18  |");
        System.out.println("   |-----|-----|-----|");
        System.out.println("   | 19  | 20  | 21  |");
        System.out.println("   |-----|-----|-----|");
        System.out.println("   | 22  | 23  | 24  |");
        System.out.println("   |-----|-----|-----|");
        System.out.println("\nSpaces on level three:\n");
        System.out.println("   |-----|-----|");
        System.out.println("   | 25  | 26  |");
        System.out.println("   |-----|-----|");
        System.out.println("   | 27  | 28  |");
        System.out.println("   |-----|-----|");
        System.out.println("\nSpace on level four:\n");
        System.out.println("   |-----|");
        System.out.println("   | 29  |");
        System.out.println("   |-----|");
        System.out.println("\nPlayer * starts!");
    };

    public void start() {
        printGameRules();
        // gameBoard.printBoard();
        while (gameBoard.checkWinner().equals(" ")) {
            gameBoard.printBoard();
            System.out.println("Current player: " + currentPlayer.getSymbol());
            Move move = askForMove();
            if (move != null) {
                move.makeMove(gameBoard);
                System.out.println("Player "+ currentPlayer.getSymbol() + " after this move has: " + currentPlayer.spheres);
                switchTurn();
            }
        }
        System.out.println("Winner is: " + currentPlayer.getSymbol());
    };

    private Move askForMove() {
        Move move = null;
        System.out.println("Select move type: place new (p) / remove existing (r) / move existing (m).  Or exit game (e) / restart (re)");
        String moveInput;
        int spaceInput;
        int spaceInput2;
        int moveFromSpaceInput;
        try {
            moveInput = scanner.next();
            if (!moveInput.equals("p") &&
                    !moveInput.equals("r") &&
                    !moveInput.equals("m") &&
                    !moveInput.equals("e") &&
                    !moveInput.equals("re")){
                System.out.println(
                        "Invalid input!!!!!");
                return null;
            } else {
                switch(moveInput){
                    case ("p"):
                        System.out.println("Choose space to put your sphere: ");
                        try{
                            spaceInput = scanner.nextInt();
                            if ((spaceInput >= 0) && gameBoard.freeSpots.contains(spaceInput)){
                                move = new PlaceNew(spaceInput, currentPlayer);
                            } else {
                                System.out.println(
                                        "Select different space!");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println(
                                    "Invalid input!! (first move)");
                            break;
                        }

                        break;
                    case ("r"):
                        if (gameBoard.checkRemoveRule(currentPlayer)){
                            System.out.println(gameBoard.checkRemoveRule(currentPlayer));
                            int amountToRemove;
                            boolean validMove = false;

                            while (!validMove) {
                                System.out.println("Enter amount to remove (1 or 2):");

                                try {
                                    amountToRemove = scanner.nextInt();

                                    if (amountToRemove == 1) {
                                        System.out.println("Choose a space to remove your sphere from:");
                                        spaceInput = scanner.nextInt();

                                        if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer)) {
                                            move = new RemoveExisting(spaceInput, 42, currentPlayer); // 42 as dummy
                                            validMove = true;
                                        } else {
                                            System.out.println("Invalid space. Try again.");
                                        }

                                    } else if (amountToRemove == 2) {
                                        System.out.println("Choose two spaces to remove your spheres from:");
                                        spaceInput = scanner.nextInt();
                                        spaceInput2 = scanner.nextInt();

                                        if (gameBoard.checkRemoveSphere(spaceInput, currentPlayer) &&
                                                gameBoard.checkRemoveSphere(spaceInput2, currentPlayer)) {
                                            move = new RemoveExisting(spaceInput, spaceInput2, currentPlayer);
                                            validMove = true;
                                        } else {
                                            System.out.println("One or both spaces are invalid. Try again.");
                                        }

                                    } else {
                                        System.out.println("You can only remove 1 or 2 spheres.");
                                    }

                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.next(); // Clear bad input
                                }
                            }
                        } else {
                            System.out.println(
                                    "You have no spheres to remove!");
                            break;
                        }
                        break;
                    case ("m"):
                        break;
                    case ("e"):
                        System.out.println("Game exited, goodbye!");
                        System.exit(0);
                        break;
                    case ("re"):
                        gameBoard = new Board();
                        player1 = new Player("*");
                        player2 = new Player("#");
                        currentPlayer = player1;
                        start();
                        break;
                }
            };
        } catch (InputMismatchException e) {
            System.out.println(
                    "Invalid input!!");
            askForMove();
        };
        return move;
    };


    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
    // PLAY GAME HERE
    public static void main(String[] args)
    {
        Game game = new Game();
        game.start();
    }
}