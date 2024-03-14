package shared;

public class GroupMessage extends Message{
    private String groupName;

    /**
     * Constructs a new GroupMessage object with the specified message body, user, and group name.
     *
     * @param messageBody the body of the message
     * @param user        the user who sent the message
     * @param groupName   the group to which the message was sent
     */
    public GroupMessage(String messageBody, User user, String groupName) {
        super(messageBody, user);
        this.groupName = groupName;
    }

    /**
     * Returns the group name associated with this message.
     *
     * @return the group name as a String
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * Returns a string representation of the GroupMessage object.
     * The string representation includes the user's name, the group name, and the message body.
     *
     * @return a string representation of the GroupMessage object.
     */
    @Override
    public String toString() {
        return "[" + this.getGroupName() + "] " + this.getUser() + ": " + this.getMessageBody();
    }
}
