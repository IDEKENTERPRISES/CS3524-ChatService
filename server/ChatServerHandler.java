package server;

import shared.User;
import shared.requests.Request;
import shared.responses.Response;
import shared.Topic;

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
	private User user;

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
		this.user = null;

		try {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the user associated with this ChatServerHandler.
	 * This is assigned by the server
	 *
	 * @param user the user to associate with this ChatServerHandler
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns the username of the user associated with this ChatServerHandler.
	 *
	 * @return the username of the user associated with this ChatServerHandler
	 * 	   or "Unknown user" if the user is null
	 */
	private String getUsername() {
		return this.user == null ? "Unknown user" : this.user.getUserName();
	}

	/**
	 * Core server thread loop
	 */
	@Override
	public void run() {
		try {
			System.out.println("User has connected");
			while (true) {
				Object message = inputStream.readObject();

				ConnectionPool actualPool = this.pool.get();
				if (actualPool == null) {
					System.out.println("Connection pool is null");
					break;
				}

				if (message instanceof Request) {
					// Handle request
					Request request = (Request) message;
					request.execute(this, actualPool);
				} else {
					System.out.println("Unknown message type received");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("IOException or ClassNotFoundException occured in inputStream.readObject for user: " + this.getUsername());
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				System.out.println("IOException occured in socket.close for user: " + this.getUsername());
			} finally {
				ConnectionPool actualPool = pool.get();
				if (actualPool != null) {
					actualPool.removeHandler(this);
				}
			}
		}
	}

	/**
	 * Checks message for any Topic keywords
	 * 
	private Boolean processMessage(String message, ConnectionPool actualPool) {
		for (Topic topic : actualPool.getTopics()) {
			if (message.contains(topic.getTopicName())) {
					return true;
				}
			}
			return false;
		}
	*/

	/**
	 * Sends a response to the client
     */
	public void sendResponse(Response response) {
		try {
			this.outputStream.writeObject(response);
		} catch (IOException e) {
			System.out.println("IOException occured in outputStream.writeObject for user: " + this.getUsername());
		}
	}

	/**
	 * Returns the client associated with this ChatServerHandler.
	 *
	 * @return the client
	 */
	public User getUser() {
		return this.user;
	}
}
