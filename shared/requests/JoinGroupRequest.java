package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class JoinGroupRequest extends Request {
    private String groupName;

    public JoinGroupRequest(String groupName) {
        this.groupName = groupName;
    }

    public JoinGroupRequest(Matcher matcher) {
        this(matcher.group(1));
    }

    public JoinGroupRequest() {
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
        group.addMember(handler.getUser());

        this.sendOKResponse(handler, pool, "Joined group successfully.");
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("^JOIN (\\w+)$");
    }
}
