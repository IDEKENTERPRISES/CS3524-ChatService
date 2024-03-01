package client;

import java.util.Scanner;

/**
 * This class serves as the entry point for the EchoClient application.
 * It creates an instance of EchoClient and starts the client.
 */
public class RunClient {
	public static void main(String[] args) {
		// Ask user for server hostname and port number
        Scanner scanner = new Scanner(System.in);
        // Ask for new port number
		String hostname = "localhost";
		int port = 42096;

		// Ask for new hostname
        System.out.print("Enter server hostname (default locahost): ");
		String newHostname = scanner.nextLine();

		if (!newHostname.isEmpty()) {
			// New hostname provided
			hostname = newHostname;
		}

		try {
			// Ask for new port number
            System.out.print("Enter server port number (default 42069): ");
			String portStr = scanner.nextLine();

			if (!portStr.isEmpty()) {
				// New port number provided
				port = Integer.parseInt(portStr);
			}
		} catch (NumberFormatException e) {
			// Invalid port number
			System.out.println("Invalid port number. Using default port.");
		}

		// Create a new EchoClient instance with the given/default hostname and port number.
		// "localhost" indicates that the server is running on the same machine as the client.
		Client client = new Client(hostname, port);

		// Start the EchoClient, which will attempt to connect to the server and enable
		// the user to send messages to be echoed back.
		client.run();
	}
}
