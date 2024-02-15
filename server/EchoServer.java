package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * The EchoServer class sets up a server that echoes messages received from clients.
 * It uses TCP/IP sockets for communication.
 */
public class EchoServer {

    private int port; // Port number on which the server will listen.
    private ObjectInputStream streamFromClient; // Stream to read objects from the client.
    private ObjectOutputStream streamToClient; // Stream to write objects to the client.
    private ServerSocket serverSocket; // Server socket that waits for client connections.

    /**
     * Constructor that initializes the EchoServer with a specific port.
     *
     * @param port The port number for the server to listen on.
     */
    public EchoServer(int port) {
        this.port = port;
        this.streamFromClient = null;
        this.streamToClient = null;
    }

    /**
     * Sets up the server socket with the specified port.
     *
     * @throws IOException If an I/O error occurs when opening the socket.
     */
    private void setup() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
    }

    /**
     * Waits for a client to connect and sets up the input and output streams.
     *
     * @throws IOException If an I/O error occurs when waiting for a connection.
     */
    private void awaitClient() throws IOException {
        System.out.println("Waiting for client...");
        Socket socket = this.serverSocket.accept(); // Accepts a connection from a client.

        System.out.println("Client connected.");

        // Initializes the streams for communication with the client.
        this.streamFromClient = new ObjectInputStream(socket.getInputStream());
        this.streamToClient = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Processes messages from the client and sends back an echoed message.
     *
     * @throws IOException If an I/O error occurs while reading or writing the message.
     */
    private void processResult() throws IOException {
        String result = "**NO RESULT**"; // Default response if no message is received.
        try {
            // Reads a message from the client.
            String readString = (String) this.streamFromClient.readObject();
            System.out.println("Received from client: " + readString);

            // Echoes the message back to the client, prefixed with "[Messenger]".
            result = "[Messenger] " + readString;
        } catch (ClassNotFoundException e) {
            System.out.println("Unrecognised class."); // Error if the sent object's class is unknown.
        }
        // Sends the result back to the client.
        streamToClient.writeObject(result);
    }

    /**
     * Main method that runs the server, accepting client connections and processing messages.
     */
    public void run() {
        try {
            this.setup();
            while (true) { // Main loop to keep server running.
                this.awaitClient();
                try {
                    while (true) { // Loop to handle messages from a single client.
                        this.processResult();
                    }
                } catch (EOFException | SocketException e) {
                    System.out.println("Client disconnected."); // Client has disconnected.
                } catch (IOException e) {
                    System.out.println("IO exception: " + e.getMessage()); // General I/O error handling.
                } finally {
                    // Close streams and socket when client disconnects.
                    try {
                        if (streamFromClient != null) streamFromClient.close();
                        if (streamToClient != null) streamToClient.close();
                    } catch (IOException e) {
                        System.out.println("Error closing streams: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage()); // Error handling for server setup.
        }
    }
}
