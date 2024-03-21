package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.RegisterResponse;

public class RegisterRequest extends Request {
	private String userName;

	public RegisterRequest(String userName) {
		this.userName = userName;
	}

	public RegisterRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	public RegisterRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (this.isAuthorized(handler)) {
			this.sendErrorResponse(handler, pool, "Already logged in.");
			return;
		}
		if (pool.getUser(this.userName) != null) {
			this.sendErrorResponse(handler, pool, "Username already taken.");
			return;
		}
		var user = new User(this.userName);
		handler.setUser(user);
		handler.sendResponse(new RegisterResponse(user));
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^REGISTER (\\w+)$");
	}

}
