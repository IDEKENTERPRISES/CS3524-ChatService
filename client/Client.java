package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * The EchoClient class is used to connect to an EchoServer and send messages which
 * are then echoed back by the server.
 */
public class Client {

	private String host; // The hostname or IP address of the server.
	private int port; // The port number of the server to connect to.
	private Socket socket; // Socket for communicating with the server.
	private ObjectOutputStream streamToServer; // Stream to send messages to the server.
	private ObjectInputStream streamFromServer; // Stream to receive messages from the server.

	/**
	 * Constructor to initialize the EchoClient with server details.
	 *
	 * @param host The hostname or IP address of the server.
	 * @param port The port number of the server.
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		this.socket = null;
		this.streamToServer = null;
		this.streamFromServer = null;
	}

	/**
	 * Establishes a connection to the server and initializes the communication streams.
	 *
	 * @throws IOException If an I/O error occurs when creating the socket or streams.
	 */
	private void connect() throws IOException {
		try {
			// Create a socket and initialize the streams.
			this.socket = new Socket(this.host, this.port);
			this.streamToServer = new ObjectOutputStream(this.socket.getOutputStream());
			this.streamFromServer = new ObjectInputStream(this.socket.getInputStream());
		} catch (UnknownHostException e) {
			// Handle unknown host exceptions.
			System.out.println("Unrecognised host: " + this.host);
			System.out.println("Aborting...");
			throw new IOException(e);
		}

		System.out.println("Connected to server at " + this.host + ":" + this.port);
	}

	/**
	 * Sends a message to the server and prints the server's response.
	 *
	 * @param message The message to send to the server.
	 * @throws IOException            If an I/O error occurs when sending or receiving the message.
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	private void sendMessageToServer(String message) throws IOException, ClassNotFoundException {
		// Send the message to the server.
		this.streamToServer.writeObject(message);
		// Wait for and print the server's response.
		String result = (String) this.streamFromServer.readObject();
		System.out.println("Received from server: " + result);
	}

	/**
	 * The main client method that handles input from the user and communicates with the server.
	 */
	public void run() {
		try {
			System.out.println("Attempting to connect to server at " + this.host + ":" + this.port + "...");
			// Connect to the server.
			this.connect();
			// Create a Scanner to read user input.
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					// Prompt the user for a message.
					System.out.println("Input message here (type 'exit' to quit): ");
					String message = scanner.nextLine();
					// Exit the loop if the user types 'exit'.
					if ("exit".equalsIgnoreCase(message)) {
						break;
					}
					// Send the message to the server.
					this.sendMessageToServer(message);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// Handle exceptions related to I/O and class resolution.
			System.out.println("Encountered an error: " + e.getMessage());
		} finally {
			// Close all resources upon finishing.
			try {
				if (this.streamToServer != null) this.streamToServer.close();
				if (this.streamFromServer != null) this.streamFromServer.close();
				if (this.socket != null) this.socket.close();
			} catch (IOException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}

	/**
	 * The entry point of the client application.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		// Create an EchoClient instance and start it.
		Client client = new Client("localhost", 50000);
		client.run();
	}
}
