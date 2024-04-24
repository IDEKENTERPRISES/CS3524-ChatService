package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.Group;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMessageRequest extends MessageRequest {
	private String targetName;

	public GroupMessageRequest(String messageBody, String targetName) {
		super(messageBody);
		this.targetName = targetName;
	}

	@SuppressWarnings("unused")
	public GroupMessageRequest(Matcher matcher) {
		this(matcher.group(2), matcher.group(1));
	}

	@SuppressWarnings("unused")
	public GroupMessageRequest() {
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
		this.sendErrorResponse(handler, pool, "Group not found.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SENDGROUP (\\w+) (.+)$");
	}

    @Override
    public String toString() {
        return ("SENDGROUP " + targetName + " " + getMessageBody());
    }
}
