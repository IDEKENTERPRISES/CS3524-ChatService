package shared;

import java.util.HashSet;
import java.util.Set;


public class Topic {
    private final String topicName;
    private final Set<User> subscribers;

    /**
     * Constructs a new Topic object with the specified keyword of the topic and subscribers.
     * 
     * @param topicName the name of the topic
     */
    public Topic(String topicName) {
        this.topicName = topicName;
        this.subscribers = new HashSet<>();
    }

    /**
     * Subscribes a user to the topic.
     * 
     * @param user the subscriber to be added
     */
    public void addSubscriber(User user) {
        this.subscribers.add(user);
    }

    /**
     * Checks if a user is already a subscriber of the topic
     * 
     * @param user the user to check
     */
    public boolean hasSubscriber(User user) {
        return this.subscribers.contains(user);
    }

    /**
     * Returns the name of the topic.
     * 
     * @return the name of the topic as a String.
     */
    public String getTopicName() {
        return this.topicName;
    }

    /**
     * Returns the subscribers of the topic.
     * 
     * @return the subscribers of the topic as a Set.
     */
    public Set<User> getSubscribers() {
        return this.subscribers;
    }

    /**
     * Returns a string representation of the Topic object. 
     * 
     * @return a string representation of the Topic object.
     */
    @Override
    public String toString() {
        StringBuilder memberString = new StringBuilder();
        for (User user : subscribers) {
            memberString.append(user).append(", ");
        }
        return memberString.toString();
    }
}
