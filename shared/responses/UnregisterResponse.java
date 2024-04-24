package shared.responses;

import client.Client;
import shared.User;

public class UnregisterResponse extends Response {
	private final User user;

	public UnregisterResponse(User user) {
		this.user = user;
	}

	@Override
	public void execute(Client client) {
		client.setUser(user);
        System.out.printf("Server: %s has unregistered.%n", user.getUserName());
	}
}
