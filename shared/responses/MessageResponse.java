package shared.responses;

import client.Client;
import shared.User;

public class MessageResponse extends Response {
	private User source;
	private String messageBody;

	public MessageResponse(User source, String messageBody) {
		this.source = source;
		this.messageBody = messageBody;
	}

	@Override
	public void execute(Client client) {
		System.out.println(source.getUserName() + ": " + this.messageBody);
	}
}
