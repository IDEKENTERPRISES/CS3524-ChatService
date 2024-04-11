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
	private User user;
	
	public ListTopicsRequest() {
		this.user = null;
	}

	@SuppressWarnings("unused")
	public ListTopicsRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		
		this.user = handler.getUser();
		handler.sendResponse(new ListTopicsResponse(this.user, pool.getTopics()));
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^TOPICS$");
	}
}
