package client.net;

import common.LoginRequest;
import common.LoginResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection {

    public Socket connection;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public ServerConnection(String ip, int port) throws IOException {
        try {
            this.connection = new Socket(ip, port);
            setStreams();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void setStreams() throws IOException {
        out = new ObjectOutputStream(connection.getOutputStream());
        out.flush();
        in = new ObjectInputStream(connection.getInputStream());
    }

    public void sendLogin(LoginRequest loginRequest) throws IOException {
        out.writeObject(loginRequest);
        out.flush();
        out.reset();
    }

    public LoginResponse getLoginResponse() throws IOException, ClassNotFoundException {
        return (LoginResponse) in.readObject();
    }

}
