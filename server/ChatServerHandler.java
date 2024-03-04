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
					Command command = (Command) message;
					switch (command.getCommand()) {
						case "REGISTER":
							if (this.userName != null) {
								this.sendObjectToClient(new Command("REGISTER", null, "SERVER"));
								break;
							}
							if (actualPool.isUsernameTaken(command.getArgs()[0])) {
								this.sendObjectToClient(new Command("REGISTER", null, "SERVER"));
								break;
							}
							this.userName = command.getArgs()[0];

							this.sendObjectToClient(new Command("REGISTER", new String[]{this.userName}, "SERVER"));

							System.out.printf("User %s registered\n", this.userName);
							break;
						case "UNREGISTER":
							if (this.userName == null) {
								this.sendObjectToClient(new Command("UNREGISTER", null, "SERVER"));
								break;
							}
							this.sendObjectToClient(new Command("UNREGISTER", new String[]{this.userName}, "SERVER"));

							this.userName = null;

							System.out.println("User unregistered");
							break;
						case "GETUSERS":
							String[] users = actualPool.getUserList();
							this.sendObjectToClient(new Command("GETUSERS", users, "SERVER"));
							break;
						default:
							break;
					}
				} else if (message instanceof Message) {
					if (this.getClientName() == null) {
						this.sendObjectToClient(new Message("You are not registered, use the REGISTER command.", "SERVER"));
					} else {
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

	public void sendObjectToClient(Object msg) {
		try {
			this.outputStream.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace(); // TODO handle this error
		}
	}

	public String getClientName() {
		return this.userName;
	}
}
