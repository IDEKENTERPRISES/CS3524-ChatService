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
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^UNSUBSCRIBE (\\w+)$");
	}

}
