package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		for (User user : group.getMembers()) {
			user.sendResponse(pool, new MessageResponse(user, "Group " + group.getGroupName() + " has been removed."));
		}

		pool.removeGroup(group);
		this.sendOKResponse(handler, pool, "Group removed successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^REMOVE (\\w+)$");
	}

    @Override
    public String toString() {
        return "REMOVE " + groupName;
    }
}
