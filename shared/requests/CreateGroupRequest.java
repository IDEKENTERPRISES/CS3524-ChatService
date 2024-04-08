package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class CreateGroupRequest extends Request {
	private String groupName;

	public CreateGroupRequest(String groupName) {
		this.groupName = groupName;
	}

	@SuppressWarnings("unused")
	public CreateGroupRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public CreateGroupRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}

		if (pool.getGroup(this.groupName) != null) {
			this.sendErrorResponse(handler, pool, "Group name already taken.");
			return;
		}

		pool.createGroup(groupName);
		this.sendOKResponse(handler, pool, "Group created successfully.");

		var joinRequest = new JoinGroupRequest(groupName);
		joinRequest.execute(handler, pool);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^CREATE (\\w+)$");
	}
}
