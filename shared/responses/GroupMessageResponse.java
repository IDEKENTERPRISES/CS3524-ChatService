package shared.responses;

import client.Client;
import shared.User;

public class GroupMessageResponse extends Response {
	private final User source;
	private final String messageBody;
    private final String group;

	public GroupMessageResponse(String group, User source, String messageBody) {
		this.source = source;
		this.messageBody = messageBody;
        this.group = group;
	}

	@Override
	public void execute(Client client) {
		System.out.println("[" + group + "] " + source.getUserName() + ": " + this.messageBody);
	}
}
