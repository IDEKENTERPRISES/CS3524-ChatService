package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class UnregisterRequest extends Request{
	public UnregisterRequest(Matcher matcher) {
	}

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
