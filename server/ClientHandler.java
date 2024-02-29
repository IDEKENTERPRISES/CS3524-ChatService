package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ConnectionPool pool;

    public ClientHandler(Socket clientSocket, ConnectionPool pool) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(this.clientSocket.getInputStream());
        }
        catch (IOException e) {
        }
        finally {
            this.close();
        }
    }

    private void close() {

    }
}
