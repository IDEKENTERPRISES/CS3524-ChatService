package shared;

public class DirectMessage extends Message{
    private String recipient;

    /**
     * Constructs a new DirectMessage object with the specified message body, user, and recipient.
     *
     * @param messageBody the body of the message
     * @param user        the user who sent the message
     * @param recipient   the recipient of the message
     */
    public DirectMessage(String messageBody, String user, String recipient) {
        super(messageBody, user);
        this.recipient = recipient;
    }

    /**
     * Returns the recipient associated with this message.
     *
     * @return the recipient as a String
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * Returns a string representation of the DirectMessage object.
     * The string representation includes the user's name, the recipient's name, and the message body.
     *
     * @return a string representation of the DirectMessage object.
     */
    @Override
    public String toString() {
        return this.getUser() + " -> " + this.getRecipient() + ": " + this.getMessageBody();
    }
}
