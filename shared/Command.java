package shared;

import java.io.Serializable;

public class Command implements Serializable {
    public static final String[] keywords = new String[] {
		"REGISTER",
		"UNREGISTER",
		"GETUSERS",
		"SEND",
		"CREATE",
		"JOIN",
		"LEAVE",
		"REMOVE"
	};

    private final String command;
    private final String[] args;
    private final String username;

    /**
     * Constructs a new Command object with the specified command, arguments, and
     * username.
     *
     * @param command  the command to be executed
     * @param args     the arguments for the command
     * @param username the username of the user executing the command
     */
    public Command(String command, String[] args, String username) {
        this.command = command;
        this.args = args;
        this.username = username;
    }

    /**
     * Returns the command associated with this object.
     *
     * @return the command as a string
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Returns the arguments of the command.
     *
     * @return an array of strings representing the arguments of the command
     */
    public String[] getArgs() {
        return this.args;
    }

    /**
     * Returns the username associated with this command.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns a string representation of the Command object.
     *
     * @return a string representation of the Command object.
     */
    @Override
    public String toString() {
        String argString = "";

        if (this.args != null) {
            for (String arg : this.args) {
                argString += arg + " ";
            }
        }

        return String.format("Command: %s, Args: %s, Username: %s", this.command, argString, this.username);
    }
}
