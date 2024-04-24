package shared.requests;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicMessageRequest extends MessageRequest {
	public TopicMessageRequest(String messageBody) {
		super(messageBody);
	}

	@SuppressWarnings("unused")
	public TopicMessageRequest(Matcher matcher) {
		this(matcher.group(1));
	}

	@SuppressWarnings("unused")
	public TopicMessageRequest() {
		// only for RequestFactory
	}

	@Override
	public void execute(ChatServerHandler handler, ConnectionPool pool) {
		if (!this.checkAuthorizationAndSendError(handler, pool)) {
			return;
		}
		var response = new MessageResponse(handler.getUser(), this.getMessageBody());

		// topics handling
		this.createTopicsFromHashes(pool);
		Set<User> recipients = this.getPotentialRecipients(pool);
		recipients.remove(handler.getUser());
		recipients.stream()
			.forEach(r -> r.sendResponse(pool, response));
	}

	protected Set<User> getPotentialRecipients(ConnectionPool pool) {
		Set<User> recipients = new HashSet<>();
		pool.getTopics().stream()
			.filter(t -> this.getMessageBody().contains(t.getTopicName()))
			.forEach(t -> recipients.addAll(t.getSubscribers()));
		return recipients;
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SHOUTTOPIC (.+)$");
	}

    @Override
    public String toString() {
        return "SHOUTTOPIC " + getMessageBody();
    }
}
