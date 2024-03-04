package server;

import shared.Message;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private final List<ChatServerHandler> connections = new ArrayList<>();

    // add ChatServerHandler into a list
    public void addConnection(ChatServerHandler handler){
        connections.add(handler);
    }

    // broadcast messages
    public void broadcast(Message message) {
        for (ChatServerHandler handler: this.connections){
            String clientName = handler.getClientName();
             if (clientName != null && !clientName.equals(message.getUser())) {
                System.out.println("Relaying to " + handler.getClientName());
                handler.sendObjectToClient(message);
            }
        }
    }

    public void removeUser(ChatServerHandler handler) {
        // remove the user's connection handler from pool
        connections.remove(handler);
    }

    public boolean isUsernameTaken(String username) {
        for (ChatServerHandler handler: this.connections){
            if (handler.getClientName() != null && handler.getClientName().equals(username)) {
                return true;
            }
        }
        return false;
    }

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