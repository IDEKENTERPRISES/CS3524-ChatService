package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListTopicsRequest extends Request {

	public ListTopicsRequest() {

	}

	@SuppressWarnings("unused")
	public ListTopicsRequest(Matcher matcher) {
		this();
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}

		handler.sendResponse(new MessageResponse(pool.SERVER, "TOPICS: " + pool.getTopicsAsString()));
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPICS$");
	}

    @Override
    public String toString() {
        return "TOPICS";
    }
}
