package client;

import java.util.regex.Matcher;

import shared.requests.*;

public class RequestFactory {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Request>[] requestClasses = new Class[] {
		RegisterRequest.class,
		UnregisterRequest.class,
		CreateGroupRequest.class,
		JoinGroupRequest.class,
		LeaveGroupRequest.class,
		RemoveGroupRequest.class,
		TargetedMessageRequest.class,
		CreateTopicRequest.class,
		SubscribeTopicRequest.class,
		UnsubscribeTopicRequest.class,
		ListTopicsRequest.class,
		GlobalMessageRequest.class
	};

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
				continue;
			}
		}
		return null;
	}
}
