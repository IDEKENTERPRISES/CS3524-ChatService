package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeaveGroupRequest extends Request {
	private String groupName;

	public LeaveGroupRequest(String groupName) {
		this.groupName = groupName;
	}

	@SuppressWarnings("unused")
	public LeaveGroupRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public LeaveGroupRequest() {
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

		group.removeMember(handler.getUser());
		this.sendOKResponse(handler, pool, "Left group successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^LEAVE (\\w+)$");
	}

    @Override
    public String toString() {
        return ("LEAVE " + groupName);
    }
}
