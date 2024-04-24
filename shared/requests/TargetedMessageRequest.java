package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.Group;
import shared.User;
import shared.responses.PrivateMessageResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		Group group = pool.getGroup(this.targetName);
		if (group != null) {
            group.sendGroupMessage(handler, pool, this.getMessageBody());
			return;
		}

		var user = pool.getUser(this.targetName);
		if (user != null) {
			this.sendUserMessage(handler, pool, user);
			return;
		}

		this.sendErrorResponse(handler, pool, "User or group not found.");
	}

	private void sendUserMessage(ChatServerHandler handler, ConnectionPool pool, User user) {
		var response = new PrivateMessageResponse(user, handler.getUser(), this.getMessageBody());
		user.sendResponse(pool, response);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SEND (\\w+) (.+)$");
	}

    @Override
    public String toString() {
        return "SEND " + targetName + " " + getMessageBody();
    }
}
