package server;

import shared.Message;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private final List<ChatServerHandler> connections = new ArrayList<>();

    /**
     * Adds a connection to the connection pool.
     *
     * @param handler the ChatServerHandler representing the connection to be added
     */
    public void addConnection(ChatServerHandler handler){
        connections.add(handler);
    }


    /**
     * Broadcasts a message to all connected clients except the sender.
     *
     * @param message the message to be broadcasted
     */
    public void broadcast(Message message) {
        for (ChatServerHandler handler: this.connections){
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
     * Checks if a username is already taken by any connected client.
     *
     * @param username the username to check
     * @return true if the username is already taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        for (ChatServerHandler handler: this.connections){
            if (handler.getClientName() != null && handler.getClientName().equals(username)) {
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
        for (ChatServerHandler handler: this.connections){
            if (handler.getClientName() != null) {
                users.add(handler.getClientName());
            }
        }
        return users.toArray(new String[0]);
    }
}