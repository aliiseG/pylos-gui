package com.example.demo;

public class Space {
    private int spaceNumber;
    private String spacePlayer;
    private Boolean spaceAvailability;
    private int spaceLevel;
    private int spaceRow;
    private int spaceCol;

    Space(int nr, String player, Boolean availability, int spaceLevel, int spaceRow, int spaceCol){
        this.spaceNumber = nr;
        this.spacePlayer = player;
        this.spaceAvailability = availability;
        this.spaceLevel = spaceLevel;
        this.spaceRow = spaceRow;
        this.spaceCol = spaceCol;
    }

    // setter and getter for spacePlayer
    public void setSpaceNumber(int spaceNumber){
        this.spaceNumber = spaceNumber;
    }
    public int getSpaceNumber(){
        return this.spaceNumber;
    }

    // setter and getter for spacePlayer
    public void setSpacePlayer(String playerSymbol){
        this.spacePlayer = playerSymbol;
    }
    public String getSpacePlayer(){
        return this.spacePlayer;
    }

    // setter and getter for spaceAvailability
    public void setSpaceAvailability(Boolean availability){
        this.spaceAvailability = availability;
    }
    public Boolean getSpaceAvailability(){
        return this.spaceAvailability;
    }
    // getters for spaceLevel, spaceRow and spaceCol
    public int getSpaceLevel(){
        return this.spaceLevel;
    }
    public int getSpaceRow(){
        return this.spaceRow;
    }

    public int getSpaceCol(){
        return this.spaceCol;
    }
}

