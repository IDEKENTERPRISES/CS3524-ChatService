package server;

import shared.Group;
import shared.Message;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private final List<ChatServerHandler> connections = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    public static int NOTFOUND = -1;
    public static int USER = 0;
    public static int GROUP = 1;

    /**
     * Adds a connection to the connection pool.
     *
     * @param handler the ChatServerHandler representing the connection to be added
     */
    public void addConnection(ChatServerHandler handler) {
        connections.add(handler);
    }

    /**
     * Creates a group in the group list.
     *
     * @param groupName the group to be added
     * @param owner     the owner of the group
     * @return true if the group was created, false otherwise
     */
    public boolean createGroup(String groupName, String owner) {
        if (isUsernameTaken(groupName)) {
            return false;
        }
        Group group = new Group(groupName, owner);
        this.groups.add(group);
        return true;
    }

    /**
     * Removes a group from the group list, only if it has less than 2 members.
     *
     * @param groupName the group to be removed
     * @return true if the group was removed, false otherwise
     */
    public boolean removeGroup(String groupName) {
        for (Group g : this.groups) {
            if (g.getGroupName().equals(groupName)) {
                if (g.getMembers().length < 2) {
                    this.groups.remove(g);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes a group from the group list, only if it has no members.
     *
     * @param group the group to be removed
     * @return true if the group was removed, false otherwise
     */
    public boolean removeGroup(Group group) {
        if (group.getMembers().length < 1) {
            this.groups.remove(group);
            return true;
        }
        return false;
    }

    /**
     * Adds a user to a group.
     *
     * @param user the user to be added
     * @param group the group to add the user to
     * @return true if the user was added, false otherwise
     */
    public boolean addUserToGroup(String user, String group) {
        for (Group g : this.groups) {
            if (g.getGroupName().equals(group)) {
                if (g.isMember(user)) {
                    return false;
                }
                g.addMember(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a user from a group.
     *
     * @param user  the user to be removed
     * @param group the group to remove the user from
     * @return true if the user was removed, false otherwise
     */
    public boolean removeUserFromGroup(String user, String group) {
        for (Group g : this.groups) {
            if (g.getGroupName().equals(group)) {
                if (!g.isMember(user)) {
                    return false;
                }
                g.removeMember(user);
                if (g.getMembers().length < 1) {
                    removeGroup(g);
                    System.out.println("Group " + group + " removed");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if string is a group name or a username.
     *
     * @param name the string to check
     * @return 0 if the string is a username, 1 if the string is a group name, -1 if
     *         not found
     */
    public int checkRecipient(String name) {
        for (Group group : this.groups) {
            if (group.getGroupName().equals(name)) {
                return GROUP;
            }
        }
        for (ChatServerHandler handler : this.connections) {
            if (handler.getClientName() != null && handler.getClientName().equals(name)) {
                return USER;
            }
        }
        return NOTFOUND;
    }

    /**
     * Sends a message to a specified user
     *
     * @param message
     */
    public void sendToUser(Message message, String recipient) {
        for (ChatServerHandler handler : this.connections) {
            if (handler.getClientName() != null && handler.getClientName().equals(recipient)) {
                handler.sendObjectToClient(message);
            }
        }
    }

    /**
     * Sends a message to a specified group
     *
     * @param message
     * @param recipient
     * @return true if the message was sent, false otherwise
     */
    public boolean sendToGroup(Message message, String recipient) {
        for (Group group : this.groups) {
            if (group.getGroupName().equals(recipient)) {
                if (!group.isMember(message.getUser())) {
                    return false;
                }
                for (String member : group.getMembers()) {
                    for (ChatServerHandler handler : this.connections) {
                        if (handler.getClientName() != null && handler.getClientName().equals(member)) {
                            handler.sendObjectToClient(message);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     *
     * @param message the message to be broadcasted
     */
    public void broadcast(Message message) {
        for (ChatServerHandler handler : this.connections) {
            String clientName = handler.getClientName();
            if (clientName != null && !clientName.equals(message.getUser())) {
                // System.out.println("Relaying to " + handler.getClientName());
                handler.sendObjectToClient(message);
            }
        }
    }

    /**
     * Removes the specified ChatServerHandler from the connection pool.
     *
     * @param handler the ChatServerHandler to be removed
     */
    public void removeUser(ChatServerHandler handler) {
        connections.remove(handler);
    }

    /**
     * Checks if a username is already taken by any connected client or group.
     *
     * @param username the username to check
     * @return true if the username is already taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        for (ChatServerHandler handler : this.connections) {
            if (handler.getClientName() != null && handler.getClientName().equals(username)) {
                return true;
            }
        }
        for (Group group : this.groups) {
            if (group.getGroupName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array of strings containing the names of all connected users.
     *
     * @return an array of strings representing the names of all connected users
     */
    public String[] getUserList() {
        List<String> users = new ArrayList<>();
        for (ChatServerHandler handler : this.connections) {
            if (handler.getClientName() != null) {
                users.add(handler.getClientName());
            }
        }
        return users.toArray(new String[0]);
    }
}
