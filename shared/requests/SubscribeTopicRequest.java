package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class SubscribeTopicRequest extends Request {
	private String topicName;

	public SubscribeTopicRequest(String topicName) {
		this.topicName = topicName;
	}

	@SuppressWarnings("unused")
	public SubscribeTopicRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public SubscribeTopicRequest() {
		// only for RequestFactory
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

		if (topic.hasSubscriber(handler.getUser())) {
			this.sendErrorResponse(handler, pool, "You are already subscribed to this topic.");
			return;
		}

		topic.addSubscriber(handler.getUser());
		this.sendOKResponse(handler, pool, "Subscribed successfully.");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SUBSCRIBE (\\w+)$");
	}
}
