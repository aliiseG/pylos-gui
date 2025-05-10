package com.example.demo;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private ServerSocket serverSocket;
    private Socket player1Socket;
    private Socket player2Socket;
    private BufferedReader in1, in2;
    private PrintWriter out1, out2;
    private Board board;
    private Player player1, player2;
    private Player currentPlayer;
    private PylosGame gameInstance;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for players...");

        player1Socket = serverSocket.accept();
        out1 = new PrintWriter(player1Socket.getOutputStream(), true);
        in1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
        out1.println("Connected as Player *");

        player2Socket = serverSocket.accept();
        out2 = new PrintWriter(player2Socket.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        out2.println("Connected as Player #");

        System.out.println("Both players connected. Starting game.");


        //board = new Board();
        player1 = new Player("*");
        player2 = new Player("#");
        //currentPlayer = player1;

//        // runGame();
        gameInstance = new PylosGame(this);
        gameInstance.startGame();

    }

    public void broadcast(String msg) {
        out1.println(msg);
        out2.println(msg);
    }

    public String sendToPlayer(Player targetPlayer, String msg){
        String moveInput = null;
        try {
            PrintWriter out = targetPlayer.getSymbol().equals("*") ? out1 : out2;
            BufferedReader in = targetPlayer.getSymbol().equals("*") ? in1 : in2;

            out.println(msg);
            if (msg.startsWith("INPUT:")){
                moveInput = in.readLine();
                return moveInput;
            } else return null;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void shutdown() throws IOException {
        in1.close(); in2.close();
        out1.close(); out2.close();
        player1Socket.close(); player2Socket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        new GameServer().start(1234);
    }
}
