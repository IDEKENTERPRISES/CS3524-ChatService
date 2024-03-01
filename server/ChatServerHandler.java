package server;

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
			this.userName = (String) inputStream.readObject();
			System.out.printf("User %s connected\n", this.userName);
			while (true) { // TODO should be replaced by a loop with an end condition
				Message message = (Message) inputStream.readObject();
				String messageBody = message.getMessageBody();
				this.userName = message.getUser();
				System.out.println(message);
				ConnectionPool actualPool = pool.get();
				if (actualPool == null) {
					System.out.println("Connection pool is null");
					break;
				}
				if (messageBody.equalsIgnoreCase("exit")) { // TODO refactor this into a separate message type
					actualPool.removeUser(this);					
					break;
				}
				actualPool.broadcast(message);
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

	public void sendMessageToClient(Message msg) {
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
