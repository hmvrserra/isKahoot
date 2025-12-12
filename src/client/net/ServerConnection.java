package client.net;

import client.gui.IsKahootClient;
import common.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection extends Thread {

    public Socket connection;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    private IsKahootClient client;
    private boolean running = true;

    public ServerConnection(String ip, int port, IsKahootClient client) {
        try {
            this.connection = new Socket(ip, port);
            this.client = client;
            setStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            while(running){
                Object received  = in.readObject();
                if(received instanceof QuestionDTO){
                    QuestionDTO question = (QuestionDTO) received;
                    SwingUtilities.invokeLater(() -> client.showQuestion(question));
                } else if(received instanceof ScoreboardDTO){
                    ScoreboardDTO scoreboard = (ScoreboardDTO) received;
                    SwingUtilities.invokeLater(() -> client.showScoreboard(scoreboard));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    public void closeConnection(){
        running = false;
        try {
            if(connection != null)
                connection.close();
            if(in != null)
                in.close();
            if(out != null)
                out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setStreams() throws IOException {
        out = new ObjectOutputStream(connection.getOutputStream());
        out.flush();
        in = new ObjectInputStream(connection.getInputStream());
    }

    public boolean login(String username, String team, String room) throws IOException, ClassNotFoundException {
        LoginRequest loginRequest = new LoginRequest(username, team, room);
        out.writeObject(loginRequest);
        out.flush();

        Object respondeObj = in.readObject();
        if(respondeObj instanceof LoginResponse){
            LoginResponse loginResponse = (LoginResponse) respondeObj;
            if(loginResponse.isSuccess()){
                this.start();
                return true;
            }
        }
        return false;
    }

    public void sendAnswer(int answerIndex) {
        try {
            Answer answer = new Answer(answerIndex);
            out.writeObject(answer);
            out.flush();
            out.reset();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
