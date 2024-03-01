package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The EchoServer class sets up a server that echoes messages received from clients.
 * It uses TCP/IP sockets for communication.
 */
public class Server {

	private final int port; // Port number on which the server will listen.
	private ServerSocket serverSocket; // Server socket that waits for client connections.
	private ConnectionPool pool; // Connection pool to manage client connections.

	/**
	 * Constructor that initializes the EchoServer with a specific port.
	 *
	 * @param port The port number for the server to listen on.
	 */
	public Server(int port) {
		this.port = port;
		this.serverSocket = null;
	}

	/**
	 * Sets up the server socket with the specified port.
	 *
	 * @throws IOException If an I/O error occurs when opening the socket.
	 */
	private void setup() throws IOException {
		this.serverSocket = new ServerSocket(this.port);
		this.pool = new ConnectionPool();
	}

	/**
	 * Waits for a client to connect and sets up the input and output streams.
	 */
	private ChatServerHandler awaitClient() {
		System.out.println("Listening for client connections on port " + this.port + "...");
		try {
			Socket socket = this.serverSocket.accept(); // Accepts a connection from a client.
			System.out.println("Client connected.");
			ChatServerHandler handler = new ChatServerHandler(socket, this.pool);
			this.pool.addConnection(handler);
			return handler;
		} catch (IOException e) {
			System.out.println("Error accepting client connection: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Main method that runs the server, accepting client connections and processing messages.
	 */
	public void run() {
		try {
			this.setup();
		} catch (IOException e) {
			System.out.println("Server exception: " + e.getMessage()); // Error handling for server setup.
		}
		while (true) {
			ChatServerHandler handler = this.awaitClient();
			if (handler == null) {
                System.out.println("Handler is null...");
				break;
			}
			Thread thread = new Thread(handler);
			thread.start();
		}
		System.out.println("Server shutting down.");
	}
}
