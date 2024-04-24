package shared.requests;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.User;
import shared.responses.MessageResponse;

public class TargetedTopicMessageRequest extends MessageRequest {
	private String targetName;
	public TargetedTopicMessageRequest(String messageBody, String targetName) {
		super(messageBody);
	}

	@SuppressWarnings("unused")
	public TargetedTopicMessageRequest(Matcher matcher) {
		this(matcher.group(2), matcher.group(1));
	}

	@SuppressWarnings("unused")
	public TargetedTopicMessageRequest() {
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
		System.out.println(recipients.size());
		recipients.stream()
			.forEach(r -> r.sendResponse(pool, response));
	}

	private Set<User> getPotentialRecipients(ConnectionPool pool) {
		Set<User> recipients = new HashSet<>();
		pool.getTopics().stream()
			.filter(t -> this.getMessageBody().contains(t.getTopicName()))
			.forEach(t -> recipients.addAll(t.getSubscribers()));
		return recipients;
	}

	@Override
	public Pattern getPattern() {
		return Pattern.compile("^SENDTOPIC (\\w+) (.+)$");
	}
}
