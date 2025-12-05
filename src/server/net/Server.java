package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 2025;

    private ServerSocket server; // Server

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

    public static void main(String[] args) {
        Server server = new Server();
        server.runServer();
    }
}
