package client;

/**
 * This class serves as the entry point for the EchoClient application.
 * It creates an instance of EchoClient and starts the client.
 */
public class RunEchoClient {
    public static void main(String[] args) {
        // Create a new EchoClient instance with the server's hostname and port number.
        // "localhost" indicates that the server is running on the same machine as the client.
        // 50000 is the port number where the server is listening for connections.
        EchoClient client = new EchoClient("localhost", 50000);

        // Start the EchoClient, which will attempt to connect to the server and enable
        // the user to send messages to be echoed back.
        client.run();
    }
}
