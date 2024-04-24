package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListUsersRequest extends Request {

    public ListUsersRequest() {
        // Constructor for RequestFactory
    }

    @SuppressWarnings("unused")
    public ListUsersRequest(Matcher matcher) {
        // Constructor for parsing user input
    }

    @Override
    public void execute(ChatServerHandler handler, ConnectionPool pool) {
        if (!this.checkAuthorizationAndSendError(handler, pool)) {
            return;
        }

        // Collect usernames of connected users
        String userList = pool.getUsers().stream()
                .map(User::getUserName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No users online");

        // Send response to the requesting client
        this.sendOKResponse(handler, pool, "Current users: " + userList);
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("^LIST$");
    }

    @Override
    public String toString() {
        return "LIST";
    }
}