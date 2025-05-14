package com.example.demo;



import java.util.ArrayList;
import java.util.List;

public class Board {
    public List<Space> boardSpaces = new ArrayList<>();
    public List<Integer> freeSpots = new ArrayList<>();

    // create new game board class object with empty board and free spot list
    public Board(){
        createNewBoard();
        checkAvailableSpaces();
    };
    public void createNewBoard(){
        int m = 0;
        for (int j = 1; j<5;j++){
            for (int k = 1; k<5; k++){
                boardSpaces.add(new Space(m, " ", true, 1,j,k));
                m++;
            }
        };

        for (int j = 1; j<4;j++){
            for (int k = 1; k<4; k++){
                boardSpaces.add(new Space(m, " ", false, 2,j,k));
                m++;
            }
        };

        for (int j = 1; j<3;j++){
            for (int k = 1; k<3; k++){
                boardSpaces.add(new Space(m, " ", false, 3,j,k));
                m++;
            }
        };

        boardSpaces.add(new Space(m, " ", false, 4,1,1));
    };
    // check if there is a winner
    public String checkWinner(){
        if (boardSpaces.get(29).getSpacePlayer()!=" "){
            return boardSpaces.get(29).getSpacePlayer();
        }else return " ";
    };
    // check and update array that contains numbers of all spaces available for placing a sphere
    public void checkAvailableSpaces(){
        for (Space space : boardSpaces){
            if(space.getSpacePlayer()==" " && space.getSpaceAvailability()==true){
                if(!freeSpots.contains(space.getSpaceNumber())){
                    freeSpots.add(space.getSpaceNumber());
                }
            }else if (freeSpots.contains(space.getSpaceNumber())){
                freeSpots.remove(Integer.valueOf(space.getSpaceNumber()));
            }
        }
    };
    // check if spot on specified level, row and col is empty
    public Boolean isEmpty(int level, int row, int col){
        for (Space space: boardSpaces){
            if (space.getSpaceLevel()==level && space.getSpaceRow()==row && space.getSpaceCol()==col){
                if (space.getSpacePlayer()!=" ") return false;
            }
        }
        return true;
    };

    public Space getSpaceByPlacement(int level, int nr){
        for (Space space: boardSpaces){
            if (space.getSpaceLevel()==level && space.getSpaceNumber()==nr){
                return space;
            }
        }
        return null;
    }

    public void updateAvailability(){
        for (Space space: boardSpaces){
            int level = space.getSpaceLevel();
            int row = space.getSpaceRow();
            int col = space.getSpaceCol();
            switch(level){
                case 1:
                    // check if level 2 has put a shpere on top of this sphere, if yes, then make availability false
                    if (!isEmpty(level + 1, row - 1, col - 1) ||
                            !isEmpty(level + 1, row - 1, col) ||
                            !isEmpty(level + 1, row, col - 1) ||
                            !isEmpty(level + 1, row, col)) {
                        space.setSpaceAvailability(false);
                    };
                    checkAvailableSpaces();
                    break;
                case 2:
                    // check if 1. level lower has 4 spheres placed below this, then make availability true, else if either of them are empty and the spot was previously made available, then switch back to false
                    if (!isEmpty(level-1, row, col) &&
                            !isEmpty(level-1, row, col+1) &&
                            !isEmpty(level-1, row+1, col) &&
                            !isEmpty(level-1, row+1, col+1)){
                        space.setSpaceAvailability(true);
                    }else if ((isEmpty(level-1, row, col) ||
                            isEmpty(level-1, row, col+1) ||
                            isEmpty(level-1, row+1, col) ||
                            isEmpty(level-1, row+1, col+1)) && space.getSpaceAvailability()==true){
                        space.setSpaceAvailability(false);
                    };

                    //2. level higher has put a shpere on top of this sphere, if yes, then make availability false, else if the spaces on top are empty and none on the bottom are empty and it was previously made unavailable, then make true
                    if (!isEmpty(level + 1, row-1, col-1) ||
                            !isEmpty(level + 1, row-1, col) ||
                            !isEmpty(level + 1, row, col-1) ||
                            !isEmpty(level + 1, row, col)){
                        space.setSpaceAvailability(false);
                    }else if (
                            (isEmpty(level + 1, row-1, col-1) &&
                                    isEmpty(level + 1, row-1, col) &&
                                    isEmpty(level + 1, row, col-1) &&
                                    isEmpty(level + 1, row, col)) &&
                                    (!isEmpty(level-1, row, col) &&
                                            !isEmpty(level-1, row, col+1) &&
                                            !isEmpty(level-1, row+1, col) &&
                                            !isEmpty(level-1, row+1, col+1)) && space.getSpaceAvailability()==false){
                        space.setSpaceAvailability(true);
                    };
                    checkAvailableSpaces();
                    break;
                case 3:
                    // check if 1. level lower has 4 spheres placed below this, then make availability true, 2. level 2 has put a shpere on top of this sphere if yes then make availability false
                    if (!isEmpty(level-1, row, col) &&
                            !isEmpty(level-1, row, col+1) &&
                            !isEmpty(level-1, row+1, col) &&
                            !isEmpty(level-1, row+1, col+1)){
                        space.setSpaceAvailability(true);
                    };
                    checkAvailableSpaces();
                    break;
                case 4:
                    // check if all 3rd level spaces are taken (space.getSpacePlayer!=" ") if yes than make availability true
                    if (!isEmpty(level-1, 1, 1) &&
                            !isEmpty(level-1, 1, 2) &&
                            !isEmpty(level-1, 2, 1) &&
                            !isEmpty(level-1, 2, 2)){
                        space.setSpaceAvailability(true);
                    };
                    checkAvailableSpaces();
                    break;
            }
        }
    };
    // place player specific sphere in the selected space
    public void putNewSphere(int spaceNr, Player player){
        boardSpaces.get(spaceNr).setSpacePlayer(player.getSymbol());
        updateAvailability();
        checkAvailableSpaces();
    };

    // remove a sphere from a non-empty space and add it back to specific players sphere count
    public void removeSphere(int spaceNr, Player player){
        boardSpaces.get(spaceNr).setSpacePlayer(" ");
        updateAvailability();
        checkAvailableSpaces();
    };

    // FOR PRINTING PLAYABLE BOARD
    public Boolean checkLevelAvailibility(int level){
        for (Space space : boardSpaces){
            if (space.getSpaceLevel()==level && space.getSpaceAvailability()==true){
                return true;
            }
        }
        return false;
    };
    public boolean checkRemoveRule(Player player){
        for (int i = 0; i<29; i++){
            if (i<16){
                if (boardSpaces.get(i).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+1).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+4).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+5).getSpacePlayer().equals(player.getSymbol())){
                    return true; }
            }
            else if (i>15 && i<25){
                if (boardSpaces.get(i).getSpacePlayer().equals(player.getSymbol())&&
                        boardSpaces.get(i+1).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+3).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+4).getSpacePlayer().equals(player.getSymbol())){
                    return true;
                }
            }
            else if (i>24 && i<29){
                if (boardSpaces.get(i).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+1).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+2).getSpacePlayer().equals(player.getSymbol()) &&
                        boardSpaces.get(i+3).getSpacePlayer().equals(player.getSymbol())){
                    return true;
                }
            }

        }
        return false;
    };

    public boolean checkRemoveSphere(int spaceNr, Player player){
        if (spaceNr < 0 || spaceNr > 29) return false;
        Space space = boardSpaces.get(spaceNr);
        return (space.getSpacePlayer().equals(player.getSymbol()) && space.getSpaceAvailability());
    }

//    // print first level with player symbols in spaces
//    public void printFirstLevel(){
//        System.out.println("First level:");
//        System.out.println("   |---|---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(0).getSpacePlayer() +" | "+ boardSpaces.get(1).getSpacePlayer() +" | "+ boardSpaces.get(2).getSpacePlayer() +" | "+ boardSpaces.get(3).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(4).getSpacePlayer() +" | "+ boardSpaces.get(5).getSpacePlayer() +" | "+ boardSpaces.get(6).getSpacePlayer() +" | "+ boardSpaces.get(7).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(8).getSpacePlayer() +" | "+ boardSpaces.get(9).getSpacePlayer() +" | "+ boardSpaces.get(10).getSpacePlayer() +" | "+ boardSpaces.get(11).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(12).getSpacePlayer() +" | "+ boardSpaces.get(13).getSpacePlayer() +" | "+ boardSpaces.get(14).getSpacePlayer() +" | "+ boardSpaces.get(15).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|---|");
//    };
//
//    // print second level with player symbols in spaces
//    public void printSecondLevel(){
//        System.out.println("Second level:");
//        System.out.println("   |---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(16).getSpacePlayer() +" | "+ boardSpaces.get(17).getSpacePlayer() +" | "+ boardSpaces.get(18).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(19).getSpacePlayer() +" | "+ boardSpaces.get(20).getSpacePlayer() +" | "+ boardSpaces.get(21).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|");
//        System.out.println("   | "+ boardSpaces.get(22).getSpacePlayer() +" | "+ boardSpaces.get(23).getSpacePlayer() +" | "+ boardSpaces.get(24).getSpacePlayer() +" |");
//        System.out.println("   |---|---|---|");
//    };
//    // print third level with player symbols in spaces
//    public void printThirdLevel(){
//        System.out.println("Third level:");
//        System.out.println("   |---|---|");
//        System.out.println("   | "+ boardSpaces.get(25).getSpacePlayer() +" | "+ boardSpaces.get(26).getSpacePlayer() +" |");
//        System.out.println("   |---|---|-");
//        System.out.println("   | "+ boardSpaces.get(27).getSpacePlayer() +" | "+ boardSpaces.get(28).getSpacePlayer() +" |");
//        System.out.println("   |---|---|");
//    };
//
//    public void printFourthLevel(){
//        System.out.println("Fourth level:");
//        System.out.println("   |---|");
//        System.out.println("   | "+ boardSpaces.get(29).getSpacePlayer() +" |");
//        System.out.println("   |---|");
//    };
//    public void printBoard(){
//        if (checkLevelAvailibility(4) == true){printFourthLevel();};
//        if (checkLevelAvailibility(3) == true){printThirdLevel();};
//        if (checkLevelAvailibility(2) == true){printSecondLevel();};
//        printFirstLevel();
//    };
    private String getFirstLevelString() {
        return
                "First level:\n" +
                        "   |---|---|---|---|\n" +
                        "   | " + boardSpaces.get(0).getSpacePlayer() + " | " + boardSpaces.get(1).getSpacePlayer() + " | " + boardSpaces.get(2).getSpacePlayer() + " | " + boardSpaces.get(3).getSpacePlayer() + " |\n" +
                        "   |---|---|---|---|\n" +
                        "   | " + boardSpaces.get(4).getSpacePlayer() + " | " + boardSpaces.get(5).getSpacePlayer() + " | " + boardSpaces.get(6).getSpacePlayer() + " | " + boardSpaces.get(7).getSpacePlayer() + " |\n" +
                        "   |---|---|---|---|\n" +
                        "   | " + boardSpaces.get(8).getSpacePlayer() + " | " + boardSpaces.get(9).getSpacePlayer() + " | " + boardSpaces.get(10).getSpacePlayer() + " | " + boardSpaces.get(11).getSpacePlayer() + " |\n" +
                        "   |---|---|---|---|\n" +
                        "   | " + boardSpaces.get(12).getSpacePlayer() + " | " + boardSpaces.get(13).getSpacePlayer() + " | " + boardSpaces.get(14).getSpacePlayer() + " | " + boardSpaces.get(15).getSpacePlayer() + " |\n" +
                        "   |---|---|---|---|\n";
    };
    private String getSecondLevelString() {
        return
                "Second level:\n" +
                        "   |---|---|---|\n" +
                        "   | " + boardSpaces.get(16).getSpacePlayer() + " | " + boardSpaces.get(17).getSpacePlayer() + " | " + boardSpaces.get(18).getSpacePlayer() + " |\n" +
                        "   |---|---|---|\n" +
                        "   | " + boardSpaces.get(19).getSpacePlayer() + " | " + boardSpaces.get(20).getSpacePlayer() + " | " + boardSpaces.get(21).getSpacePlayer() + " |\n" +
                        "   |---|---|---|\n" +
                        "   | " + boardSpaces.get(22).getSpacePlayer() + " | " + boardSpaces.get(23).getSpacePlayer() + " | " + boardSpaces.get(24).getSpacePlayer() + " |\n" +
                        "   |---|---|---|";
    };
    private String getThirdLevelString() {
        return
                "Third level:\n" +
                        "   |---|---|\n" +
                        "   | " + boardSpaces.get(25).getSpacePlayer() + " | " + boardSpaces.get(26).getSpacePlayer() + " |\n" +
                        "   |---|---|\n" +
                        "   | " + boardSpaces.get(27).getSpacePlayer() + " | " + boardSpaces.get(28).getSpacePlayer() + " |\n" +
                        "   |---|---|";
    };
    private String getFourthLevelString() {
        return
                "Fourth level:\n" +
                        "   |---|\n" +
                        "   | " + boardSpaces.get(29).getSpacePlayer() + " |\n" +
                        "   |---|";
    };
    public String getBoardString() {
        StringBuilder board = new StringBuilder();

        if (checkLevelAvailibility(4)) {
            board.append(getFourthLevelString()).append("\n");
        }
        if (checkLevelAvailibility(3)) {
            board.append(getThirdLevelString()).append("\n");
        }
        if (checkLevelAvailibility(2)) {
            board.append(getSecondLevelString()).append("\n");
        }

        board.append(getFirstLevelString());
        return board.toString();
    }
//    public String getCurrentGameboard(){
//        String currentGameboard = "";
//        for (int i = 0; i < boardSpaces.size(); i++){
//            currentGameboard = currentGameboard.concat(boardSpaces.get(i).getSpacePlayer() + ",");
//        }
//        return currentGameboard;
//    }
    public String getCurrentGameboard() {
        StringBuilder currentGameboard = new StringBuilder();
        for (int i = 0; i < boardSpaces.size(); i++) {
            Space space = boardSpaces.get(i);
            String player = space.getSpacePlayer();

            // add to string if space is taken or available
            if (!player.equals(" ")) {
                currentGameboard.append(i).append(":").append(player).append(",");
            }else if (space.getSpaceAvailability()) {
                currentGameboard.append(i).append(":").append("available").append(",");
            }
        }
        return currentGameboard.toString();
    }


}
