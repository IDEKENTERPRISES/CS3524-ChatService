package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class RemoveGroupRequest extends Request {
	private String groupName;

	public RemoveGroupRequest(String groupName) {
		this.groupName = groupName;
	}

	@SuppressWarnings("unused")
	public RemoveGroupRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public RemoveGroupRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}

		var group = pool.getGroup(this.groupName);
		if (group == null) {
			this.sendErrorResponse(handler, pool, "Group does not exist.");
			return;
		}

		pool.removeGroup(group);
		this.sendOKResponse(handler, pool, "Group removed successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^REMOVE (\\w+)$");
	}

}
