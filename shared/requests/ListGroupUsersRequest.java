package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListGroupUsersRequest extends Request {
	private String groupName;

	public ListGroupUsersRequest(String groupName) {
		this.groupName = groupName;
	}

	@SuppressWarnings("unused")
	public ListGroupUsersRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public ListGroupUsersRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		var group = pool.getGroup(groupName);
		if (group == null) {
			this.sendErrorResponse(handler, pool, "Group '" + groupName + "' does not exist");
			return;
		}
		this.sendOKResponse(handler, pool, "Users in group '" + group.getGroupName() + "': " + group);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^LISTGROUP (\\w+)$");
	}

    @Override
    public String toString() {
        return "LISTGROUP " + groupName;
    }
}
