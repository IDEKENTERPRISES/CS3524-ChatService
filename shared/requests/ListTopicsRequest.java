package shared.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shared.Topic;
import shared.User;

import shared.responses.ListTopicsResponse;

import server.ChatServerHandler;
import server.ConnectionPool;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ListTopicsRequest extends Request {
	
	public ListTopicsRequest() {

	}

	@SuppressWarnings("unused")
	public ListTopicsRequest(Matcher matcher) {
		this();
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		
		User user = handler.getUser();
		handler.sendResponse(new ListTopicsResponse(user, pool.getTopics()));
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPICS$");
	}
}
