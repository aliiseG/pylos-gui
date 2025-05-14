package com.example.demo;

public class Player {
    private String symbol;
    private int spheres = 15;

    public Player(String symbol){
        this.symbol = symbol;
    };

    public String getSymbol(){
        return symbol;
    };
    // if player places new sphere on board
    public void useSphere() {
        spheres--;
    }
    // if player removes a sphere from the board
    public void returnSphere() {
        spheres++;
    }
    public int getSphereCount(){
        return spheres;
    }
    public boolean hasSpheres() {
        return spheres > 0;
    }
}
