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

	/**
	 * Returns a set of all connected users.
	 *
	 * @return a set of all connected users
	 */
	public Set<User> getUsers() {
		return handlers.stream()
			.map(h -> h.getUser())
			.filter(u -> u != null)
			.collect(Collectors.toSet());
	}

	/**
	 * Creates a group in the group list.
	 *
	 * @param groupName the name of the group to be added
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

	/**
	 * Gets the group with the given groupName
	 * @param groupName the name of the group to get
	 * @return the group with the given groupName, or null if no such group exists
	 */
	public Group getGroup(String groupName) {
		for (Group group : groups) {
			System.out.println(groupName + " " + group.getGroupName() + " : " + group.getGroupName().equals(groupName));
		}
		return groups.stream()
			.filter(g -> g.getGroupName().equals(groupName))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Gets the user with the given username
	 * @param username the username of the user to get
	 * @return the user with the given username, or null if no such user exists
	 */
	public User getUser(String username) {
		return handlers.stream()
			.map(h -> h.getUser())
			.filter(u -> u != null && u.getUserName().equals(username))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Gets the ChatServerHandler for the given user
	 * @param user the user to get the ChatServerHandler for
	 * @return the ChatServerHandler for the given user, or null if no such handler exists
	 */
	public ChatServerHandler getHandler(User user) {
		return handlers.stream()
			.filter(h -> user.equals(h.getUser()))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Removes a group from the group list,
	 * should only be used if the group has no members, does not notify users
	 * TODO These checks are currently not performed!
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
	 * @return true if the handler was removed, false otherwise
	 */
	public boolean removeHandler(ChatServerHandler handler) {
		return this.handlers.remove(handler);
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
