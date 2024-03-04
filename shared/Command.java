package shared;

import java.io.Serializable;

public class Command implements Serializable {
    public static final String[] keywords = new String[] {
        "REGISTER",
        "UNREGISTER",
        "GETUSERS"};

    private final String command;
    private final String[] args;
    private final String username;

    public Command(String command, String[] args, String username) {
        this.command = command;
        this.args = args;
        this.username = username;
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String getUsername() {
        return this.username;
    }

    public String toString() {
        String argString = "";

        if (this.args != null) {
            for (String arg: this.args) {
                argString += arg + " ";
            }
        }

        return String.format("Command: %s, Args: %s, Username: %s", this.command, argString, this.username);
    }
}