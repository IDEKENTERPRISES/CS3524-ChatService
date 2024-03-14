package shared;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String groupName;
	private List<User> members;

    /**
     * Constructs a new Group object with the specified group name and members.
     *
     * @param groupName the name of the group
     * @param owner   the owner of the group
     */
    public Group(String groupName, User creator) {
		this.groupName = groupName;
		this.members = new ArrayList<User>();
		this.members.add(creator);
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
     * @return an array of strings representing the members of the group
     */
    public List<User> getMembers() {
        return this.members;
    }

    /**
     * Returns a string representation of the Group object.
     *
     * @return a string representation of the Group object.
     */
    @Override
    public String toString() {
		String memberString = "";
		for (User user : members) {
			memberString += user + ", ";
		}
		return memberString;
    }
}
