package shared.requests;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.Topic;
import shared.User;
import shared.responses.MessageResponse;

public abstract class MessageRequest extends Request {
	private String messageBody;

	public MessageRequest(String messageBody) {
		this.messageBody = messageBody;
	}

	public MessageRequest() {
		// only for RequestFactory
	}

	public String getMessageBody() {
		return this.messageBody;
	}

	// Tokenizes the message to check for hashtags, returns any words that had a hashtag
	protected Set<String> getHashTokens() {
		Set<String> tokens = new HashSet<String>();
		Set<String> hashtagTokens = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer(this.getMessageBody(), " ");
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

	/**
	 * Creates any new topics if there are hashtags in the message
	 * @param pool
	 * @return true if any topics were created
	 */
	protected boolean createTopicsFromHashes(ConnectionPool pool) {
		var tokens = this.getHashTokens();
		var created = false;
		for (String string : tokens) {
			if (!pool.isTopic(string)) {
				created = true;
				pool.createTopic(string);
			}
		}
		return created;
	}


}
