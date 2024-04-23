package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;
import shared.Topic;

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
		boolean isTopic = false;
		
		// Gets tokenizes
		List<String> tokens = this.getTokens(this.getMessageBody());
		if (tokens.size() != 0) {
			for (String s : tokens) {
				if (pool.isTopic(s) == false) {
					pool.createTopic(s);
					this.sendTopicMessage(handler, pool, pool.getTopic(s));
				}
				else {
					Topic currTopic = pool.getTopic(s);
					if (currTopic.hasSubscriber(handler.getUser()) == false) {
						currTopic.addSubscriber(handler.getUser());
					}
					this.sendTopicMessage(handler, pool, pool.getTopic(s));
				}
			}
		}

		else {
			// Checks to see if Topic name is in message, sendTopicMessage called if true
			for (Topic topic : pool.getTopics()) {
				if (this.getMessageBody().contains(topic.getTopicName())) {
					isTopic = true;
					this.sendTopicMessage(handler, pool, topic);
				}
			}

			// isTopic = false, sends global message as normal
			if (isTopic == false) {
				var response = new MessageResponse(handler.getUser(), this.getMessageBody());
				for (User user : pool.getUsers()) {
					if (!user.equals(handler.getUser())) {
						user.sendResponse(pool, response);
					}
				}
			}
		}
	}

	private void sendTopicMessage(ChatServerHandler handler, ConnectionPool pool, Topic topic) {
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());
			for (User subscriber : topic.getSubscribers()) {
				if(!subscriber.equals(handler.getUser())) {
					subscriber.sendResponse(pool, response);
				}
			}
	}

	// Tokenizes the message to check for hashtags, returns any words that had a hashtag
	private List<String> getTokens(String message) {
		List<String> tokens = new ArrayList<String>();
		List<String> hashtagTokens = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(message, " ");
		while (tokenizer.hasMoreElements()) {
			tokens.add(tokenizer.nextToken());
		}

		for (String s : tokens) {
			if (s.substring(0, 1).equals("#") == true) {
				hashtagTokens.add(s.substring(1));
			}
		}
		return hashtagTokens;
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^(.+)$");
	}
}
