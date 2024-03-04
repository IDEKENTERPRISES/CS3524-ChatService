package server;

import shared.Command;
import shared.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class ChatServerHandler implements Runnable {
	private final Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private final WeakReference<ConnectionPool> pool;
	private String userName;

	/**
	 * Constructs a new ChatServerHandler object.
	 * The ChatServerHandler class represents a handler for the chat server.
	 * It is responsible for handling client connections and managing communication
	 * with clients.
	 *
	 * @param socket         the socket representing the client connection
	 * @param connectionPool the connection pool for managing client connections
	 */
	public ChatServerHandler(Socket socket, ConnectionPool connectionPool) {
		System.out.println("ChatServerHandler created");
		this.socket = socket;
		this.pool = new WeakReference<>(connectionPool);
		this.userName = null;

		try {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes the main logic of the ChatServerHandler in a separate thread.
	 * This method reads incoming messages from the client, processes them, and
	 * performs the appropriate actions.
	 * If the message is a Command, it handles commands such as REGISTER,
	 * UNREGISTER, and GETUSERS.
	 * If the message is a Message, it broadcasts the message to all connected
	 * clients.
	 * If the message is of an unknown type, it prints an error message.
	 * The method also handles exceptions that may occur during the execution and
	 * closes the socket connection.
	 */
	@Override
	public void run() {
		try {
			// this.userName = (String) inputStream.readObject();
			// System.out.printf("User %s connected\n", this.userName);
			System.out.println("User has connected");
			while (true) {
				Object message = inputStream.readObject();

				ConnectionPool actualPool = this.pool.get();
				if (actualPool == null) {
					System.out.println("Connection pool is null");
					break;
				}
				if (message instanceof Command) {
					// Handle command
					Command command = (Command) message;
					switch (command.getCommand()) {
						case "REGISTER":
							// If the user is already registered, send a message to the client that they are
							// already registered.
							if (this.userName != null || actualPool.isUsernameTaken(command.getArgs()[0])) {
								this.sendObjectToClient(new Command("REGISTER", null, "SERVER"));
								break;
							}

							// Register the user
							this.userName = command.getArgs()[0];

							// Send a message to the client that they have been registered
							this.sendObjectToClient(new Command("REGISTER", new String[] { this.userName }, "SERVER"));

							System.out.printf("User %s registered\n", this.userName);
							break;
						case "UNREGISTER":
							// If the user is not registered, send a message to the client that they are not
							// registered.
							if (this.userName == null) {
								this.sendObjectToClient(new Command("UNREGISTER", null, "SERVER"));
								break;
							}

							// Send a message to the client that they have been unregistered
							this.sendObjectToClient(
									new Command("UNREGISTER", new String[] { this.userName }, "SERVER"));

							// Unregister the user
							this.userName = null;

							System.out.println("User unregistered");
							break;
						case "GETUSERS":
							// Send a list of all registered users to the client
							String[] users = actualPool.getUserList();
							this.sendObjectToClient(new Command("GETUSERS", users, "SERVER"));
							break;
						default:
							break;
					}
				} else if (message instanceof Message) {
					// Handle message

					if (this.getClientName() == null) {
						this.sendObjectToClient(
								new Message("You are not registered, use the REGISTER command.", "SERVER"));
					} else {
						// Broadcast the message to all connected and registered clients
						Message msg = (Message) message;
						System.out.printf(msg.toString());
						actualPool.broadcast(msg);
					}
				} else {
					System.out.println("Unknown message type received");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace(); // TODO handle this error
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace(); // TODO handle this error
			} finally {
				ConnectionPool actualPool = pool.get();
				if (actualPool != null) {
					actualPool.removeUser(this);
				}
			}
		}
	}

	/**
	 * Sends an object to the client.
	 *
	 * @param msg the object to be sent
	 */
	public void sendObjectToClient(Object msg) {
		try {
			this.outputStream.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace(); // TODO handle this error
		}
	}

	/**
	 * Returns the client name associated with this ChatServerHandler.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return this.userName;
	}
}
