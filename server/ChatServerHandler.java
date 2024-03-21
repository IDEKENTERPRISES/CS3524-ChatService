package server;

import shared.Command;
import shared.CommandType;
import shared.DirectMessage;
import shared.GroupMessage;
import shared.Message;
import shared.Recipient;
import shared.User;

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


	public void register(ConnectionPool pool, String[] args) {
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		if (this.user != null) {
			this.sendObjectToClient(new Message("You are already registered", pool.SERVER));
			return;
		}
		var username = args[0];
		if (pool.isUsernameTaken(username)) {
			this.sendObjectToClient(new Message("Username is already taken", pool.SERVER));
			return;
		}
		this.user = new User(username);
		this.sendObjectToClient(new Command(CommandType.REGISTER, new String[] { username }, pool.SERVER));
	}

	public void unregister(ConnectionPool pool, String[] args) {
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		if (this.user == null) {
			this.sendObjectToClient(new Message("You are not registered", pool.SERVER));
			return;
		}
		this.user = null;
		this.sendObjectToClient(new Command(CommandType.UNREGISTER, new String[] {}, pool.SERVER));
	}

	public boolean verifyRegistered(ConnectionPool pool) {
		if (this.user == null) {
			this.sendObjectToClient(new Message("You are not registered, use the REGISTER command.", pool.SERVER));
			return false;
		}
		return true;
	}

	public void getUsers(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}
		String[] users = pool.getUserList();
		this.sendObjectToClient(new Command(CommandType.GETUSERS, users, this.pool.get().SERVER));

	}

	public void send(ConnectionPool pool, String messageBody, Recipient recipient) {
		if (recipient == null) {
			this.sendObjectToClient(new Message("Unknown recipient for message", pool.SERVER));
			return;
		}

		recipient.sendMessage(pool, new Message(messageBody, user));
	}

	public void send(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}

		if (args.length != 2) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}

		var recipientName = args[0];

		var recipient = pool.getRecipient(recipientName);
		var messageText = args[1];
		this.send(pool, messageText, recipient);
	}

	public void create(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		var groupName = args[0];
		if (pool.createGroup(groupName, this.user)) {
			this.sendObjectToClient(new Message("Group " + groupName + " created", pool.SERVER));
		} else {
			this.sendObjectToClient(new Message("Group " + groupName + " already exists", pool.SERVER));
		}
	}

	public void join(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		var groupName = args[0];
		if (pool.addUserToGroup(this.user, groupName)) {
			this.sendObjectToClient(new Message("You joined group " + groupName, pool.SERVER));
		} else {
			this.sendObjectToClient(
					new Message("Group " + groupName + " does not exist or you are already part of it", pool.SERVER));
		}
	}

	public void leave(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		var groupName = args[0];
		if (pool.removeUserFromGroup(this.user, groupName)) {
			this.sendObjectToClient(new Message("You left group " + groupName, pool.SERVER));
		} else {
			this.sendObjectToClient(
					new Message("Group " + groupName + " does not exist or you are not part of it", pool.SERVER));
		}
	}

	public void remove(ConnectionPool pool, String[] args) {
		if (!this.verifyRegistered(pool)) {
			return;
		}
		if (args.length != 1) {
			this.sendObjectToClient(new Message("Invalid number of arguments", pool.SERVER));
			return;
		}
		var groupName = args[0];
		if (pool.removeGroup(groupName)) {
			this.sendObjectToClient(new Message("Group " + groupName + " removed", pool.SERVER));
		} else {
			this.sendObjectToClient(new Message("Group " + groupName + " does not exist", pool.SERVER));
		}
	}

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
						case REGISTER:
							this.register(actualPool, command.getArgs());
							break;
						case UNREGISTER:
							this.unregister(actualPool, command.getArgs());
							break;
						case GETUSERS:
							this.getUsers(actualPool, command.getArgs());
							break;
						case SEND:
							this.send(actualPool, command.getArgs());
							break;
						case CREATE:
							this.create(actualPool, command.getArgs());
							break;
						case JOIN:
							this.join(actualPool, command.getArgs());
						case LEAVE:
							this.leave(actualPool, command.getArgs());
						case REMOVE:
							this.remove(actualPool, command.getArgs());
						default:
							break;
					}
				} else if (message instanceof Message) {
					// Handle message
					Message msg = (Message) message;
					if (!this.verifyRegistered(actualPool)) {
						return;
					}
					actualPool.sendMessage(actualPool, msg);
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
	public User getClient() {
		return this.user;
	}
}
