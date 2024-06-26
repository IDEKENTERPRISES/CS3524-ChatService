package client;

import shared.requests.*;

import java.util.regex.Matcher;

public class RequestFactory {
	/**
	 * An array of classes for all the different types of requests.
	 * New request types must be added here
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends Request>[] requestClasses = new Class[] {
		RegisterRequest.class,
		UnregisterRequest.class,
		CreateGroupRequest.class,
		JoinGroupRequest.class,
		LeaveGroupRequest.class,
		RemoveGroupRequest.class,
		TargetedMessageRequest.class,
        PrivateMessageRequest.class,
        GroupMessageRequest.class,
		CreateTopicRequest.class,
		SubscribeTopicRequest.class,
		UnsubscribeTopicRequest.class,
		ListTopicsRequest.class,
        ListUsersRequest.class,
		TopicMessageRequest.class,
		TargetedTopicMessageRequest.class,
		ListGroupUsersRequest.class,
		GlobalMessageRequest.class

	};

	/**
	 * Create a request object from a string
	 * the string is matched against the pattern of each request type
	 * if a match is found, the request object is created and returned
	 *
	 * @param input The string to parse
	 * @param <T> The type of request parsed by the string
	 *
	 * @return The request object, or null if no match is found
	*/
	@SuppressWarnings("unchecked")
	public static <T extends Request> T createRequest(String input) {
		for (Class<? extends Request> requestClass : requestClasses) {
			Request instance;
			try {
				instance = requestClass.getDeclaredConstructor().newInstance();

				var matcher = instance.getPattern().matcher(input);
				if (!matcher.matches()) {
					continue;
				}

				var constructor = requestClass.getConstructor(Matcher.class);
				return (T) constructor.newInstance(matcher);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
