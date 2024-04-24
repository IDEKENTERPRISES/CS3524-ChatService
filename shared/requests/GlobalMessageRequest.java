package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.responses.MessageResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalMessageRequest extends MessageRequest {
	public GlobalMessageRequest(String messageBody) {
		super(messageBody);
	}

	@SuppressWarnings("unused")
	public GlobalMessageRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public GlobalMessageRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());

		// topics handling
		this.createTopicsFromHashes(pool);

		pool.getUsers().stream()
			.filter(r -> !handler.getUser().equals(r))
			.forEach(r -> r.sendResponse(pool, response));
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^(.+)$");
	}

    @Override
    public String toString() {
        return getMessageBody();
    }
}
