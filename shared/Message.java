package shared;

import java.io.Serializable;

public class Message implements Serializable {
	private String messageBody;
	private String user;

	public Message(String messageBody, String user) {
		this.messageBody = messageBody;
		this.user = user;
	}

	public String getMessageBody() {
		return this.messageBody;
	}

	public String getUser() {
		return this.user;
	}

	@Override
	public String toString() {
		return this.getUser() + ": " + this.getMessageBody();
	}
}
