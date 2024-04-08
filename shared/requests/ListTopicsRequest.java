package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.ChatServerHandler;
import server.ConnectionPool;

public class ListTopicsRequest extends Request {
	public ListTopicsRequest() {

	}

	@SuppressWarnings("unused")
	public ListTopicsRequest(Matcher matcher) {
		this();
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPICS$");
	}
}
