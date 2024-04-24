package shared.responses;

import client.Client;
import shared.User;

public class PrivateMessageResponse extends Response {
	private final User source;
	private final String messageBody;
    private final User target;

	public PrivateMessageResponse(User target, User source, String messageBody) {
		this.source = source;
		this.messageBody = messageBody;
        this.target = target;
	}

	@Override
	public void execute(Client client) {
		System.out.println("(" + source.getUserName() + " -> " + target + ") :" + this.messageBody);
	}
}
