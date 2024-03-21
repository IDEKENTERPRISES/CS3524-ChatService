package server;

import shared.Group;
import shared.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectionPool {
    public final User SERVER = new User("Server");
    private Set<ChatServerHandler> handlers = new HashSet<>();
    private Set<Group> groups = new HashSet<>();

    /**
     * Adds a connection to the connection pool.
     *
     * @param handler the ChatServerHandler representing the connection to be added
     */
    public void addConnection(ChatServerHandler handler) {
        handlers.add(handler);
    }

    public Set<User> getUsers() {
        return handlers.stream()
            .map(h -> h.getUser())
            .filter(u -> u != null)
            .collect(Collectors.toSet());
    }

    /**
     * Creates a group in the group list.
     *
     * @param groupName the group to be added
     * @param creator     the owner of the group
     *
     */
    public void createGroup(String groupName) {
        this.groups.add(new Group(groupName));
    }

    /**
     * Removes a group from the group list, only if it has less than 2 members.
     *
     * @param groupName the group to be removed
     * @return true if the group was removed, false otherwise
     */
    public boolean removeGroup(String groupName) {
        return this.groups.removeIf(g -> g.getGroupName().equals(groupName) && g.getMembers().size() < 2);
    }

    public Group getGroup(String groupName) {
        for (Group group : groups) {
            System.out.println(groupName + " " + group.getGroupName() + " : " + group.getGroupName().equals(groupName));
        }
        return groups.stream()
            .filter(g -> g.getGroupName().equals(groupName))
            .findFirst()
            .orElse(null);
    }

    public User getUser(String username) {
        return handlers.stream()
            .map(h -> h.getUser())
            .filter(u -> u != null && u.getUserName().equals(username))
            .findFirst()
            .orElse(null);
    }

    public ChatServerHandler getHandler(User user) {
        return handlers.stream()
            .filter(h -> user.equals(h.getUser()))
            .findFirst()
            .orElse(null);
    }

    // todo is this method necessary?
    /**
     * Removes a group from the group list, should only be used if the group has no members, does not notify users.
     *
     * @param group the group to be removed
     * @return true if the group was removed, false otherwise
     */
    public boolean removeGroup(Group group) {
        return this.groups.remove(group);
    }

    /**
     * Removes the specified ChatServerHandler from the connection pool.
     *
     * @param handler the ChatServerHandler to be removed
     */
    public void removeHandler(ChatServerHandler handler) {
        handlers.remove(handler);
    }

    /**
     * Checks if a username is already taken by any connected client or group.
     *
     * @param username the username to check
     * @return true if the username is already taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        if (handlers.stream().anyMatch(h -> h.getUser().getUserName().equals(username))) {
            return true;
        }
        return groups.stream().anyMatch(g -> g.getGroupName().equals(username));
    }
}
