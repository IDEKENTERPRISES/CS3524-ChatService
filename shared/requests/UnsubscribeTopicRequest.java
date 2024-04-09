package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class UnsubscribeTopicRequest extends Request {
	private String topicName;

	public UnsubscribeTopicRequest(String topicName) {
		this.topicName = topicName;
	}

	@SuppressWarnings("unused")
	public UnsubscribeTopicRequest() {
		// only for RequestFactory
	}

	@SuppressWarnings("unused")
	public UnsubscribeTopicRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}

		var topic = pool.getTopic(this.topicName);
		if (topic == null) {
			this.sendErrorResponse(handler, pool, "Topic does not exist.");
			return;
		}

		topic.removeSubscriber(handler.getUser());
		this.sendOKResponse(handler, pool, "Unsubscribed successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^UNSUBSCRIBE (\\w+)$");
	}

}
