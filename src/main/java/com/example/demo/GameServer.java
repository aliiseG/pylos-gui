package com.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private ServerSocket serverSocket;
    private Socket player1Socket;
    private Socket player2Socket;
    private BufferedReader in1, in2;
    private PrintWriter out1, out2;
    private PylosGame gameInstance;

    public void start(int port) throws IOException {
        // create serversocket
        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for players...");

        // wait and accept both players
        player1Socket = serverSocket.accept();
        out1 = new PrintWriter(player1Socket.getOutputStream(), true);
        in1 = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
        out1.println("PLAYER SYMBOL:Connected as Player *");

        player2Socket = serverSocket.accept();
        out2 = new PrintWriter(player2Socket.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
        out2.println("PLAYER SYMBOL:Connected as Player #");

        // create new game instance, start game
        gameInstance = new PylosGame(this);
        gameInstance.startGame();
    }

    // both players get the same message
    public void broadcast(String msg) {
        out1.println(msg);
        out2.println(msg);
    }

    // send to both players different messages
    public void notifyPlayersTurn(Player currentPlayer, Player otherPlayer) {
        sendToPlayer(currentPlayer, "TURN: Your turn!");
        sendToPlayer(otherPlayer, "WAIT TURN: Please wait for opponent..");
    }

    // send message to only one player
    public String sendToPlayer(Player targetPlayer, String msg){
        String moveInput = null;
        try {
            PrintWriter out = targetPlayer.getSymbol().equals("*") ? out1 : out2;
            BufferedReader in = targetPlayer.getSymbol().equals("*") ? in1 : in2;

            out.println(msg);
            if (msg.startsWith("INPUT:")){
                // blocks until received, gets input from client controller out move -> out.println(move) from showInputDialog
                moveInput = in.readLine();
                return moveInput;
            } else
                return null;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    // not used?
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
