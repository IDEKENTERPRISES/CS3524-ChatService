package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;

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
		pool.removeHandler(handler);
		this.sendOKResponse(handler, pool, "Unregistered successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^UNREGISTER$");
	}
}