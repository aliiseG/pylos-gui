package com.example.demo;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    private final List<Space> boardSpaces = new ArrayList<Space>();
    private final List<Integer> freeSpots = new ArrayList<>();

    // create new game board class object with empty board and free spot list
    public Board(){
        createNewBoard();
        checkAvailableSpaces();
    }
    public List<Integer> getFreeSpots() {
        return freeSpots;
    }

    // creates empty board for new game
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
                if (!Objects.equals(space.getSpacePlayer(), " ")) return false;
            }
        }
        return true;
    };

    // check the current game board, change space availability if accessible for placing a sphere
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

    // checks if the player can remove spaces
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

    // returns game baord 
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
