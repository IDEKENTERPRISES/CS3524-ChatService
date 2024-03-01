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
                handler.sendMessageToClient(message);
            }
        }
    }

    public void removeUser(ChatServerHandler handler) {
        // remove the user's connection handler from pool
        connections.remove(handler);
    }
}