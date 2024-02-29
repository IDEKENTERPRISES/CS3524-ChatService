package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;

import shared.Message;

public class ChatServerHandler implements Runnable {
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private WeakReference<ConnectionPool> pool;
	private String userName;

	public ChatServerHandler(Socket socket, ConnectionPool connectionPool) {
		this.socket = socket;
		this.pool = new WeakReference<ConnectionPool>(connectionPool);

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
			while (true) { // TODO should be replaced by a loop with an end condition
				Message message = (Message) inputStream.readObject();
				String messageBody = message.getMessageBody();
				this.userName = message.getUser();
				System.out.println(message.toString());
				if (messageBody.equalsIgnoreCase("exit")) { // TODO refactor this into a separate message type
					pool.removeUser(this);
				}
			}
		} catch () {

		} finally {

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
