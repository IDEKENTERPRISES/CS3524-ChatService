package shared.responses;

import server.ConnectionPool;
import client.Client;
import shared.User;
import shared.Topic;

public class MessageResponse extends Response {
	private final User source;
	private final String messageBody;

	public MessageResponse(User source, String messageBody) {
		this.source = source;
		this.messageBody = messageBody;
	}

	@Override
	public void execute(Client client) {
		System.out.println(source.getUserName() + ": " + this.messageBody);
	}
}
