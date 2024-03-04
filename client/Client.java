package client;

import shared.Message;
import shared.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

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

    /**
     * Represents a client that connects to a server using a specified host and
     * port.
     */
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

    /**
     * Sets up the client by establishing a connection with the server and
     * initializing input/output streams.
     */
    private void setup() {
        System.out.println("Setup started.");
        try {
            this.socket = new Socket(this.host, this.port);
            System.out.println("Connected to server.");

            this.scanner = new Scanner(System.in);
            this.outputStream = new ObjectOutputStream(
                    this.socket.getOutputStream() // Generate an output stream from the socket
            );
            this.inputStream = new ObjectInputStream(
                    this.socket.getInputStream() // Generate an input stream from the socket
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

    /**
     * Retrieves the user input message as a String.
     * 
     * @return the user input message as a String, or null if no input is received
     *         or if the user enters "exit"
     */
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
            System.err.println("No input received. Exiting...");
            this.exitFlag = true;
        }
        return messageBody;
    }

    /**
     * Sends a user message to the server, sends as either a Message or a Command
     * based on first token.
     * 
     * @param messageString the message string to be sent
     */
    private void sendUserMessage(String messageString) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(messageString); // Tokenize the message

            if (!tokenizer.hasMoreTokens()) {
                return; // If there are no tokens, return
            }

            String firstToken = tokenizer.nextToken(); // Get the first token/word

            if (Arrays.asList(Command.keywords).contains(firstToken)) {
                // If the first token is a command keyword, send a Command with the remaining
                // tokens as args
                String[] args = new String[tokenizer.countTokens()];
                for (int i = 0; i < args.length; i++) {
                    args[i] = tokenizer.nextToken();
                }
                Command userCommand = new Command(firstToken, args, this.username);
                this.outputStream.writeObject(userCommand);
            } else {
                // If the first token is not a command keyword, send a Message with the entire
                // message as the body
                Message userMessage = new Message(messageString, this.username);
                this.outputStream.writeObject(userMessage);
            }
        } catch (IOException e) {
            System.err.println("Failed communicating with the server.");
            this.exitFlag = true;
        }
    }

    /**
     * Handles user input by continuously prompting for messages and sending them to
     * the server.
     * This method runs in a loop until the exit flag is set to true.
     */
    private void handleUserInput() {
        while (!this.exitFlag) {
            String messageString = this.getUserMessage();
            if (messageString != null) {
                this.sendUserMessage(messageString);
            }
        }
    }

    /**
     * Starts a listener thread that listens to the server.
     * The listener thread is a daemon thread that terminates when the program has
     * finished.
     */
    private void startListenerThread() {
        this.listenerThread = new Thread(this::listenToServer);
        this.listenerThread.setDaemon(true);
        this.listenerThread.start();
    }

    /**
     * Starts the client by starting the listener thread and handling user input.
     */
    private void start() {
        this.startListenerThread();
        this.handleUserInput();
    }

    /**
     * Receives a message or command from the server and prints it to the console.
     * If the received object is a Message, it is printed as a string.
     * If the received object is a Command, it is processed accordingly.
     *
     * @throws IOException if an I/O error occurs while reading from the input
     *                     stream.
     */
    private void receiveObjectAndProcess() throws IOException {
        try {

            Object inObject = this.inputStream.readObject();

            if (inObject instanceof Message) {
                // If the received object is a Message, print it to the console

                Message inMessage = (Message) inObject;
                System.out.println(inMessage.toString()); // TODO: Fix printing formatting, need to clear line before
                                                          // printing message.
                System.out.print("Please input >");
            } else if (inObject instanceof Command) {
                // If the received object is a Command

                Command command = (Command) inObject;
                switch (command.getCommand()) {
                    case "REGISTER":
                        // If the command is a REGISTER command, set the username if args are present
                        if (command.getArgs() != null && command.getArgs().length > 0) {
                            this.username = command.getArgs()[0];
                            System.out.println("Registered as " + this.username);
                        } else {
                            System.out.println("Failed to register.");
                        }
                        break;
                    case "UNREGISTER":
                        // If the command is an UNREGISTER command, unset the username if args are
                        // present
                        if (command.getArgs() != null && command.getArgs().length > 0) {
                            this.username = null;
                            System.out.println("Unregistered.");
                        } else {
                            System.out.println("Failed to unregister.");
                        }
                        break;
                    case "GETUSERS":
                        // If the command is a GETUSERS command, print the users if args are present
                        if (command.getArgs() != null || command.getArgs().length > 0) {
                            System.out.println("Users: " + String.join(", ", command.getArgs()));
                        } else {
                            System.out.println("Failed to get users.");
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (NullPointerException e) {
            /*
             * this.streamFromServer was not initialised. Either:
             * (a) something went wrong during setup, or
             * (b) this.close() was called.
             * In both cases we expect this.exitFlag = true.
             */
            System.err.println("Failed to read from server.");
            this.exitFlag = true;
        } catch (ClassNotFoundException e) {
            System.err.println("Could not deserialise the message.");
        }
    }

    /**
     * Listens to the server and prints out the received messages.
     * This method runs in an infinite loop until the program is exited.
     * If an IOException occurs while receiving a message, it prints an error
     * message and continues listening.
     */
    private void listenToServer() {
        while (true) {
            try {
                // Receive an object from the server and print it
                this.receiveObjectAndProcess();
            } catch (IOException e) {
                // Close the listener if the program has exited
                if (this.exitFlag)
                    break;

                // Otherwise print an error message and keep listening
                System.err.println("Failed while listening to server.");
            }
        }
    }

    /**
     * Runs the client application by executing the setup, start, and close methods.
     */
    public void run() {
        this.setup();
        this.start();
        this.close();
    }

    /**
     * Closes the scanner and socket connections.
     * This method is responsible for closing the resources used by the client.
     * It closes the scanner and socket connections and handles any exceptions that
     * may occur.
     */
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