package com.example.demo;

public class Player {
    private String symbol;
    public int spheres = 15;

    public Player(String symbol){
        this.symbol = symbol;
    };
    public String getSymbol(){
        return symbol;
    };
    public void useSphere() {
        spheres--;
    }

    public void returnSphere() {
        spheres++;
    }

    public boolean hasSpheres() {
        return spheres > 0;
    }
}
