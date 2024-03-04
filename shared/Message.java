package shared;

import java.io.Serializable;

public class Message implements Serializable {
	private String messageBody;
	private String user;

	/**
	 * Represents a message with a message body and the user who sent it.
	 * Constructs a new Message object with the specified message body and user.
	 *
	 * @param messageBody the body of the message
	 * @param user        the user who sent the message
	 */
	public Message(String messageBody, String user) {
		this.messageBody = messageBody;
		this.user = user;
	}

	/**
	 * Returns the message body.
	 *
	 * @return the message body as a String
	 */
	public String getMessageBody() {
		return this.messageBody;
	}

	/**
	 * Returns the user associated with this message.
	 *
	 * @return the user associated with this message
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * Returns a string representation of the Message object.
	 * The string representation includes the user's name and the message body.
	 *
	 * @return a string representation of the Message object.
	 */
	@Override
	public String toString() {
		return this.getUser() + ": " + this.getMessageBody();
	}
}
