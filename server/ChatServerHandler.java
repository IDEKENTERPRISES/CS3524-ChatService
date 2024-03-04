package server;

import shared.Command;
import shared.DirectMessage;
import shared.GroupMessage;
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
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Send a list of all registered users to the client
							String[] users = actualPool.getUserList();
							this.sendObjectToClient(new Command("GETUSERS", users, "SERVER"));
							break;
						case "SEND":
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Send a message to the specified user or group
							String recipient = command.getArgs()[0];
							int recipientType = checkRecipients(recipient);

							
							String messageText = "";
							for (int i = 1; i < command.getArgs().length; i++) {
								messageText += command.getArgs()[i] + " ";
							}
							messageText = messageText.trim();
							
							System.out.println(this.userName + " SENT message to " + recipient + ": " + messageText);

							if (recipientType == -1) {
								// Send a message to the specified user or group
								this.sendObjectToClient(new Message("Recipient not found", "SERVER"));
							} else if (recipientType == 0) {
								// Send the message to the specified user
								Message msg = new DirectMessage(messageText, this.userName, recipient);
								actualPool.sendToUser(msg, recipient);
							} else if (recipientType == 1) {
								// Send the message to the specified group
								GroupMessage msg = new GroupMessage(messageText, this.userName, recipient);
								boolean groupResult = actualPool.sendToGroup(msg, recipient);
								if (!groupResult) {
									this.sendObjectToClient(new Message("Group " + recipient + " does not exist or you are not part of it", "SERVER"));
								}
							} else {
								System.out.println("Unknown recipient type");
							}
							break;
						case "CREATE":
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Create a new group
							String groupName = command.getArgs()[0];
							boolean createResult = actualPool.createGroup(groupName, this.userName);
							if (createResult) {
								this.sendObjectToClient(new Message("Group " + groupName + " created", "SERVER"));
								System.out.printf("Group %s created by %s\n", groupName, this.userName);
							} else {
								this.sendObjectToClient(new Message("Group " + groupName + " already exists", "SERVER"));
							}
							break;
						case "JOIN":
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Join an existing group
							String groupToJoin = command.getArgs()[0];
							boolean joinResult = actualPool.addUserToGroup(this.userName, groupToJoin);
							if (joinResult) {
								this.sendObjectToClient(new Message("You joined group " + groupToJoin, "SERVER"));
								System.out.printf("User %s joined group %s\n", this.userName, groupToJoin);
							} else {
								this.sendObjectToClient(new Message("Group " + groupToJoin + " does not exist or you are already part of it", "SERVER"));
							}
							break;
						case "LEAVE":
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Leave a group
							String groupToLeave = command.getArgs()[0];
							boolean leaveResult = actualPool.removeUserFromGroup(this.userName, groupToLeave);
							if (leaveResult) {
								this.sendObjectToClient(new Message("You left group " + groupToLeave, "SERVER"));
								System.out.printf("User %s left group %s\n", this.userName, groupToLeave);
							} else {
								this.sendObjectToClient(new Message("Group " + groupToLeave + " does not exist or you are not part of it", "SERVER"));
							}
							break;
						case "REMOVE":
							if (this.userName == null) {
								this.sendObjectToClient(
										new Message("You are not registered, use the REGISTER command.", "SERVER"));
								break;
							}
							// Remove a group
							String groupToRemove = command.getArgs()[0];
							boolean removeResult = actualPool.removeGroup(groupToRemove);
							if (removeResult) {
								this.sendObjectToClient(new Message("Group " + groupToRemove + " removed", "SERVER"));
								System.out.printf("Group %s removed by %s\n", groupToRemove, this.userName);
							} else {
								this.sendObjectToClient(new Message("Group " + groupToRemove + " does not exist", "SERVER"));
							}
							break;
						default:
							break;
					}
				} else if (message instanceof Message) {
					// Handle message
					Message msg = (Message) message;
					if (this.getClientName() == null) {
						this.sendObjectToClient(
								new Message("You are not registered, use the REGISTER command.", "SERVER"));
					} else {
						// Broadcast the message to all connected and registered clients
						System.out.println(msg.toString());
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
	 * Checks if the recipients of a message are registered users or groups.
	 * Recipients are specified in the first word of the message, either a group
	 * name or a user name.
	 * Uses StringTokenizer
	 * 
	 * @param message
	 * @return 0 if the recipient is a user, 1 if the recipient is a group, -1 if
	 *         the recipient is not found
	 */
	public int checkRecipients(String recipient) {
		ConnectionPool actualPool = pool.get();
		if (actualPool == null) {
			System.out.println("Connection pool is null");
			return -1;
		}

		int recipientType = actualPool.checkRecipient(recipient);
		return recipientType;
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
