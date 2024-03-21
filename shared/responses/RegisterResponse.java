package shared.responses;

import client.Client;
import shared.User;

public class RegisterResponse extends Response {
	private User user;

	public RegisterResponse(User user) {
		this.user = user;
	}

	@Override
	public void execute(Client client) {
		client.setUser(user);
		System.out.println("Successfully registered as " + user.getUserName());
	}
}
