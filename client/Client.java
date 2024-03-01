package client;

import shared.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private final String host;
    private final int port;
    private String username;
    private Socket socket;
    private Scanner scanner;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Thread listenerThread;
    private Boolean exitFlag;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.username = null;
        this.socket = null;
        this.scanner = null;
        this.inputStream = null;
        this.outputStream = null;
        this.listenerThread = null;
        this.exitFlag = null;
    }

    private void setup() {
        System.out.println("Setup started.");
        try {
            this.socket = new Socket(this.host, this.port);
            System.out.println("Connected to server.");

            this.scanner = new Scanner(System.in);
            this.outputStream = new ObjectOutputStream(
                this.socket.getOutputStream()
            );
            this.inputStream =  new ObjectInputStream(
                this.socket.getInputStream()
            );
            
            this.exitFlag = false;
            System.out.println("Setup complete!");
        } catch (UnknownHostException e) {
            System.err.println("Unknown host `" + this.host + "`.");
            this.exitFlag = true;
        } catch (IOException e) {
            System.err.println("Could not connect to the server.");
            this.exitFlag = true; // Close the program here
        }
    }

    private void registerUser() {
        if (this.exitFlag) return; // Ensure successful setup

        System.out.print("Insert user name: ");
        this.username = this.scanner.nextLine();
        try {
            this.outputStream.writeObject(this.username);
            System.out.println("Registered to server.");
        } catch (IOException e) {
            System.err.println("Encountered registering the user.");
            this.exitFlag = true;
        }
    }

    private String getUserMessage() {
        String messageBody = null; // Instantiate the return value
        System.out.print("Please input >");
        try {
            messageBody = this.scanner.nextLine();
            if (messageBody.equalsIgnoreCase("exit")) {
                this.exitFlag = true;
            }
        } catch (NoSuchElementException e) {
            // This might be thrown by `this.scanner.nextLine()` if the client 
            // exits with CTRL-C. In such case exit and return null.
            this.exitFlag = true;
        }
        return messageBody;
    }

    private void sendUserMessage(String messageString) {
        try {
            Message userMessage = new Message(messageString, this.username);
            this.outputStream.writeObject(userMessage);
        } catch (IOException e) {
            System.err.println("Failed communicating with the server.");
            this.exitFlag = true;
        }
    }

    private void handleUserInput() {
        while(!this.exitFlag) {
            String messageString = this.getUserMessage();
            if (messageString != null) { 
                this.sendUserMessage(messageString);
            }
        }
    }

    private void startListenerThread() {
        this.listenerThread = new Thread(this::listenToServer);
        // Daemon thread: terminates when the program has finished.
        this.listenerThread.setDaemon(true);
        this.listenerThread.start();
    }

    private void start() {
        this.registerUser();
        this.startListenerThread();
        this.handleUserInput();
    }

    private void receiveMessageAndPrint() throws IOException {
        try {
            Message inMessage = (Message) this.inputStream.readObject();
            System.out.print(inMessage.toString());
        } catch (NullPointerException e) {
            /* 
            this.streamFromServer was not initialised. Either:
            (a) something went wrong during setup, or
            (b) this.close() was called.
            In both cases we expect this.exitFlag = true.
            */
            this.exitFlag = true;
        } catch (ClassNotFoundException e) {
            System.err.println("Could not deserialise the message.");
        }
    }

    private void listenToServer() {
        // keep reading from server and print out.
        while (true) {
            try {
                this.receiveMessageAndPrint();
            } catch (IOException e) {
                // Close the listener if the program has exited
                if (this.exitFlag) break;

                // Otherwise print an error message and keep listening
                System.err.println("Failed while listening to server.");
            }
        }
    }

    public void run() {
        this.setup();
        this.start();
        this.close();
    }

    private void close() {
        System.out.println("Exiting...");
        try {
            this.scanner.close();
            this.socket.close();
        } catch (NullPointerException e) {
            // The setup failed, nothing to do here
        } catch (IOException e) {
            System.err.println("Failed while closing the socket.");
        }
    }
}