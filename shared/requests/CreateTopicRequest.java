package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTopicRequest extends Request{
	private String topicName;

	public CreateTopicRequest(String topicName) {
		this.topicName = topicName;
	}

	@SuppressWarnings("unused")
	public CreateTopicRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public CreateTopicRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		
		if (pool.getTopic(this.topicName) != null) {
			this.sendErrorResponse(handler, pool, "Topic already exists.");
			return;
		}

		pool.createTopic(topicName);
		this.sendOKResponse(handler, pool, "Topic created successfully.");
		
		var joinRequest = new SubscribeTopicRequest(topicName);
		joinRequest.execute(handler, pool);
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPIC (\\w+)$");
	}

    @Override
    public String toString() {
        return ("CREATE " + topicName);
    }
}
