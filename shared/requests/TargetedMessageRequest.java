package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.Group;
import shared.User;
import shared.responses.MessageResponse;

public class TargetedMessageRequest extends MessageRequest {
	private String targetName;

	public TargetedMessageRequest(String messageBody, String targetName) {
		super(messageBody);
		this.targetName = targetName;
	}

	@SuppressWarnings("unused")
	public TargetedMessageRequest(Matcher matcher) {
		this(matcher.group(2), matcher.group(1));
	}

	@SuppressWarnings("unused")
	public TargetedMessageRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.isAuthorized(handler)) {
			this.sendUnauthorizedResponse(handler, pool);
			return;
		}

		var group = pool.getGroup(this.targetName);
		if (group != null) {
			this.sendGroupMessage(handler, pool, group);
			return;
		}

		var user = pool.getUser(this.targetName);
		if (user != null) {
			this.sendUserMessage(handler, pool, user);
			return;
		}

		this.sendErrorResponse(handler, pool, "User or group not found.");
	}

	private void sendGroupMessage(ChatServerHandler handler, ConnectionPool pool, Group group) {
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());
		for (User user : group.getMembers()) {
			user.sendResponse(pool, response);
		}
	}

	private void sendUserMessage(ChatServerHandler handler, ConnectionPool pool, User user) {
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());
		user.sendResponse(pool, response);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SEND (\\w+) (.+)$");
	}
}
