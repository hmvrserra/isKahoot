package server.net;

import common.Answer;
import common.LoginRequest;
import common.LoginResponse;
import server.game.GameState;
import server.game.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket connection; // Connection
    private ObjectInputStream in; // Stream reader
    private ObjectOutputStream out; // Stream writer

    private GameState gameState = null;
    private String currentUser;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            setStreams();
            read();
        } catch (EOFException | SocketException e) {
            System.out.println("Um cliente desconectou-se");
            //Avisar gameState para tirar esse jogador
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void setStreams() throws IOException {
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    private void read() throws IOException, ClassNotFoundException, InterruptedException {
        while(true){
            Object received = in.readObject();
            if(received instanceof LoginRequest){
                LoginRequest loginRequest = (LoginRequest) received;
                currentUser = loginRequest.getUsername();
                System.out.println("Received login from: " + currentUser);
                String room = loginRequest.getRoom();
                if(!Server.gameExists(room)){
                    LoginResponse loginResponse = new LoginResponse(false, "There is no such room");
                    send(loginResponse);
                    break;
                }
                gameState = Server.getGame(room);

                if(gameState.getPlayers().containsKey(currentUser)){
                    LoginResponse loginResponse = new LoginResponse(false, "Username " + currentUser + " is already being used.");
                    send(loginResponse);
                    break;
                }
                try {
                    gameState.addPlayer(new Player(currentUser, loginRequest.getTeam(), room), this);
                } catch(Exception e){
                    LoginResponse loginResponse = new LoginResponse(false, "Can't add team, limit reached");
                    send(loginResponse);
                    break;
                }
                LoginResponse loginResponse = new LoginResponse(true, "Welcome " + currentUser);
                send(loginResponse);
            }
            else if(received instanceof Answer){
                Answer answer = (Answer) received;
                if(gameState != null && currentUser != null){
                    System.out.println("Received answer from: " + currentUser);
                    gameState.submitAnswer(currentUser, answer.getAnswer());
                }
            }
            System.out.println("Server received: " +  received.getClass().getName());
        }
    }

    public synchronized void send(Object obj) throws IOException {
        try{
            if(!connection.isClosed()){
                out.writeObject(obj);
                out.flush();
                out.reset();
            }
        } catch(IOException e) {
            System.out.println("Error during writing");
            e.printStackTrace();
        }
    }


    public void closeConnection(){
        try {
            if(connection != null)
                connection.close();
            if(in != null)
                in.close();
            if(out != null)
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
