package shared;

import server.ChatServerHandler;
import server.ConnectionPool;
import shared.responses.GroupMessageResponse;
import shared.responses.Response;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class Group {
	private final String groupName;
	private final Set<User> members;

	/**
	 * Constructs a new Group object with the specified group name and members.
	 *
	 * @param groupName the name of the group
	 */
	public Group(String groupName) {
		this.groupName = groupName;
		this.members = new HashSet<>();
	}

	/**
	 * Adds a member to the group.
	 *
	 * @param user the member to be added
	 */
	public void addMember(User user) {
		this.members.add(user);
	}

	/**
	 * Removes a member from the group.
	 *
	 * @param user the member to be removed
	 */
	public void removeMember(User user) {
		this.members.removeIf(m -> m.equals(user));
	}

	/**
	 * Checks if a user is a member of the group.
	 *
	 * @param user the user to check
	 */
	public boolean hasMember(User user) {
		return this.members.contains(user);
	}

	/**
	 * Returns the name of the group.
	 *
	 * @return the name of the group as a String
	 */
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * Returns the members of the group.
	 *
	 * @return the members of the group as a Set
	 */
	public Set<User> getMembers() {
		return this.members;
	}

    public void sendGroupMessage(ChatServerHandler handler, ConnectionPool pool, String message) {
        Response response = new GroupMessageResponse(this.getGroupName(), handler.getUser(), message);
        for (User user : this.getMembers()) {
            if (user != handler.getUser()) {
                user.sendResponse(pool, response);
            }
        }
    }

	/**
	 * Returns a string representation of the Group object.
	 *
	 * @return a string representation of the Group object.
	 */
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ");
		for (User user : members) {
			joiner.add(user.getUserName());
		}
		return joiner.toString();
	}
}
