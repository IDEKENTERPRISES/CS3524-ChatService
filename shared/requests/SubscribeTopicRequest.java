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

	public SubscribeTopicRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	public SubscribeTopicRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SUBSCRIBE (\\w+)$");
	}
}
