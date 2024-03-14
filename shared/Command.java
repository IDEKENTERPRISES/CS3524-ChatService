package shared;

import java.io.Serializable;

public class Command implements Serializable {
    private final CommandType type;
    private final String[] args;
    private final User user;

    /**
     * Constructs a new Command object with the specified command, arguments, and
     * username.
     *
     * @param command  the command to be executed
     * @param args     the arguments for the command
     * @param username the username of the user executing the command
     */
    public Command(CommandType type, String[] args, User user) {
        this.type = type;
        this.args = args;
		this.user = user;
    }

    /**
     * Returns the command associated with this object.
     *
     * @return the command as a string
     */
    public CommandType getCommand() {
        return this.type;
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
    public User getUser() {
        return this.user;
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

        return String.format("Command: %s, Args: %s, Username: %s", this.type.name(), argString, this.user);
    }
}
