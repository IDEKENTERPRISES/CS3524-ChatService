package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;

public class GlobalMessageRequest extends MessageRequest {
	public GlobalMessageRequest(String messageBody) {
		super(messageBody);
	}

	public GlobalMessageRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	public GlobalMessageRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());
		for (User user : pool.getUsers()) {
			if (!user.equals(handler.getUser())) {
				user.sendResponse(pool, response);
			}
		}
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^(.+)$");
	}
}
