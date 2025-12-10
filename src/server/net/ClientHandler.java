package server.net;

import common.LoginRequest;
import common.LoginResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket connection; // Connection
    private ObjectInputStream in; // Stream reader
    private ObjectOutputStream out; // Stream writer

    private boolean running = true;

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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void setStreams() throws IOException {
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    private void read() throws IOException, ClassNotFoundException {
        while(true){
            Object received = in.readObject();
            if(received instanceof LoginRequest){
                LoginRequest loginRequest = (LoginRequest) received;
                System.out.println("Received login from: " + loginRequest.getUsername());

                LoginResponse loginResponse = new LoginResponse(true, "Welcome " + loginRequest.getUsername());
                send(loginResponse);
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
