package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

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
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPIC (\\w+)$");
	}
}
