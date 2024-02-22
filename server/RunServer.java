package server;

/**
 * This class is the entry point for starting the EchoServer.
 * It initializes an instance of EchoServer with a specified port and starts the server.
 */
public class RunServer {
    public static void main(String[] args) {

        // Ask server admin port number on which the server will listen for incoming connections.
        int port = 42096;

        try {
            // Ask for new port number
            String portStr = System.console().readLine("Enter server port number (default 42069): ");

            if (!portStr.isEmpty()) {
                // New port number provided
                port = Integer.parseInt(portStr);
            }
        } catch (NumberFormatException e) {
            // Invalid port number
            System.out.println("Invalid port number. Using default port.");
        }

        // Create an instance of the EchoServer with the specified port number.
        Server server = new Server(port);

        // Start the server to listen for incoming client connections and process messages.
        server.run();
    }
}
