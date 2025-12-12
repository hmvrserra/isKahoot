package server.net;

import server.game.GameState;
import server.game.Quiz;
import server.game.QuizFile;
import server.game.QuizLoader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static final int PORT = 2025;
    private ServerSocket server; // Server
    private boolean running;

    private static Map<String, GameState> games = new ConcurrentHashMap<>();
    private QuizFile quizFile;

    public Server() {
        quizFile = QuizLoader.loadQuizzes();
        running = false;
    }

    public QuizFile getQuizFile() {
        return quizFile;
    }

    public void runServer(){
        try {
            server = new ServerSocket(PORT);
            while(true){
                waitForConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void waitForConnection() throws IOException {
        Socket connection = server.accept();
        ClientHandler handler =  new ClientHandler(connection);
        handler.start();

        System.out.println("Started new connection...");
    }

    private void createGame(Quiz quiz, int numTeams, int numPlayersPerTeam){
        String roomCode = createRoomCode(quiz);
        GameState gameState = new GameState(roomCode, numTeams, numPlayersPerTeam, quiz);
        games.put(roomCode, gameState);
        new Thread(() -> {
            System.out.println("Room " + roomCode + " has been created and is waiting for players.");
            try {
                gameState.awaitPlayersAndStart();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        if(!running){
            running = true;
            new Thread(() -> {
                this.runServer();
            }).start();
        }
    }

    public static synchronized GameState getGame(String room){
        return games.get(room);
    }

    public static synchronized boolean gameExists(String room){
        return games.containsKey(room);
    }

    public synchronized String createRoomCode(Quiz quiz){
        String quizName = quiz.getName();
        int random = (int) (Math.random() * 1000);
        String roomCode = quizName + "_Room" + random;
        while(gameExists(roomCode)){
            roomCode = createRoomCode(quiz);
        }
        return roomCode;
    }

    private synchronized Quiz getRandomQuiz(){
        Quiz quiz = null;
        int random = (int) (Math.random() * quizFile.getQuizzes().size());
        quiz = quizFile.getQuizzes().get(random);
        return quiz;
    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Create room: new <numTeams> <numPlayersPerTeam>");
//        System.out.println("Available quizzes: ");

        Scanner scanner = new Scanner(System.in);

        while(true) {
            String input = scanner.nextLine();
            if(input.isEmpty()) continue;

            String[] parts = input.split(" ");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "new":
                    if(parts.length != 3) {
                        System.out.println("Error: There are missing arguments.");
                        break;
                    }
                    String numTeams = parts[1];
                    String numPlayersPerTeam = parts[2];
                    Quiz quiz = server.getRandomQuiz();
                    if(quiz == null) {
                        System.out.println("Quiz not selected exist.");
                        break;
                    }
                    server.createGame(quiz, Integer.parseInt(numTeams), Integer.parseInt(numPlayersPerTeam));
            }
        }
    }
}
