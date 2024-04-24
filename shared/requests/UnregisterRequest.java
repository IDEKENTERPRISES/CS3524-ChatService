package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnregisterRequest extends Request{
	@SuppressWarnings("unused")
	public UnregisterRequest(Matcher matcher) {
	}

	@SuppressWarnings("unused")
	public UnregisterRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}

        User user = handler.getUser();
		pool.removeHandler(handler);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^UNREGISTER$");
	}

    @Override
    public String toString() {
        return "UNREGISTER";
    }
}