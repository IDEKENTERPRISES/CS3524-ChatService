package shared;

import server.ConnectionPool;

public interface Recipient {
	public void sendMessage(ConnectionPool pool, Message message);
}
