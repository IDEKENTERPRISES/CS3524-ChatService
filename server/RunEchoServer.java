package server;

/**
 * This class is the entry point for starting the EchoServer.
 * It initializes an instance of EchoServer with a specified port and starts the server.
 */
public class RunEchoServer {
    public static void main(String[] args) {
        // The port number on which the server will listen for incoming connections.
        // The value 50000 is used here as an example, but in a real-world scenario,
        // this could be passed as a command-line argument or read from a configuration file.
        int port = 50000;

        // Create an instance of the EchoServer with the specified port number.
        EchoServer server = new EchoServer(port);

        // Start the server to listen for incoming client connections and process messages.
        server.run();
    }
}
