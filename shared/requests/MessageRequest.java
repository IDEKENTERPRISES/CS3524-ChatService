package shared.requests;

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
}
