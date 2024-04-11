package shared.responses;

import client.Client;
import shared.User;
import shared.Topic;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ListTopicsResponse extends Response {
    private final User user;
    private final Set<Topic> topics;

    public ListTopicsResponse(User user, Set<Topic> topics) {
        this.user = user;
        this.topics = topics;
    }

    @Override
    public void execute(Client client) {
        client.setUser(this.user);
        System.out.println("Available topics:");
        for (Topic topic: this.topics) {
            System.out.println(topic.getTopicName());
        }
    }
}