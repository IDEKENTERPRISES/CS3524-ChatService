package shared;

public class Group {
    private String groupName;
    private String[] members;

    /**
     * Constructs a new Group object with the specified group name and members.
     *
     * @param groupName the name of the group
     * @param owner   the owner of the group
     */
    public Group(String groupName, String owner) {
        this.groupName = groupName;
        this.members = new String[1];
        this.members[0] = owner;
    }

    /**
     * Adds a member to the group.
     *
     * @param member the member to be added
     */
    public void addMember(String member) {
        String[] newMembers = new String[this.members.length + 1];
        for (int i = 0; i < this.members.length; i++) {
            newMembers[i] = this.members[i];
        }
        newMembers[this.members.length] = member;
        this.members = newMembers;
    }

    /**
     * Removes a member from the group.
     *
     * @param member the member to be removed
     */
    public void removeMember(String member) {
        String[] newMembers = new String[this.members.length - 1];
        int j = 0;
        for (int i = 0; i < this.members.length; i++) {
            if (!this.members[i].equals(member)) {
                newMembers[j] = this.members[i];
                j++;
            }
        }
        this.members = newMembers;
    }

    /**
     * Checks if a user is a member of the group.
     *
     * @param user the user to check
     */
    public boolean isMember(String user) {
        for (String member : this.members) {
            if (member.equals(user)) {
                return true;
            }
        }
        return false;
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
    public String[] getMembers() {
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
        for (int i = 0; i < this.members.length; i++) {
            memberString += this.members[i];
            if (i < this.members.length - 1) {
                memberString += ", ";
            }
        }
        return this.groupName + ": " + memberString + ".";
    }
}
