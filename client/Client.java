package client;

import shared.User;
import shared.requests.*;
import shared.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

	/* TEST */

	private final String host;
	private final int port;
	private User user;
	private Socket socket;
	private Scanner scanner;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Thread listenerThread;
	private boolean exitFlag;

	/**
	 * Represents a client that connects to a server using a specified host and
	 * port.
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		this.socket = null;
		this.scanner = null;
		this.inputStream = null;
		this.outputStream = null;
		this.listenerThread = null;
		this.exitFlag = false;
	}

	/**
	 * Creates a request object from a string.
	 * The string is matched against the pattern of each request type.
	 * If a match is found, the request object is created and returned.
	 *
	 * @param commandString the string to parse
	 * @return the request object, or null if no match is found
	 */
	private Request getRequestFromString(String commandString) {
		return RequestFactory.createRequest(commandString);
	}

	/**
	 * Sets the user associated with this client.
	 *
	 * @param user the user to associate
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sends a request to the server.
	 *
	 * @param request the request to send
	 */
	private void sendRequest(Request request) {
		try {
			this.outputStream.writeObject(request);
		} catch (IOException e) {
			System.err.println("Failed while communicating with the server.");
			this.exitFlag = true;
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
	 * Handles user input by continuously prompting for messages and sending them to
	 * the server.
	 * This method runs in a loop until the exit flag is set to true.
	 */
	private void handleUserInput() {
		while (!this.exitFlag) {
			String messageString = this.getUserMessage();
			if (!this.exitFlag && messageString != null) {
				var request = this.getRequestFromString(messageString);
				if (request == null) {
					System.err.println("Something went wrong when parsing '" + messageString + "'");
				} else {
					this.sendRequest(request);
				}
			}
		}
	}

	/**
	 * Starts the client by starting the listener thread and handling user input.
	 */
	private void start() {
		this.startListenerThread();
		this.handleUserInput();
	}

	/**
	 * Receives a response from the server and executes it.
	 * @throws IOException if inputStream.readObject throws
	 */
	private void receiveResponse() throws IOException {
		try {
			Object in = this.inputStream.readObject();
			if (in instanceof Response) {
				((Response)in).execute(this);
			} else {
				System.err.println("Unknown message type received");
			}
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
		while (!this.exitFlag) {
			try {
				// Receive an object from the server and print it
				this.receiveResponse();
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
