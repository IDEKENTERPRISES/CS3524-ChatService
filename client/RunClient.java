package client;

import java.util.Scanner;

/**
 * This class serves as the entry point for the Client Chat Server application.
 * It creates an instance of RunClient and starts the client.
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
			System.out.print("Enter server port number (default 42096): ");
			String portStr = scanner.nextLine();

			if (!portStr.isEmpty()) {
				// New port number provided
				port = Integer.parseInt(portStr);
			}
		} catch (NumberFormatException e) {
			// Invalid port number
			System.out.println("Invalid port number. Using default port.");
		}

		// Create an instance of the Client with the specified hostname and port number.
		Client client = new Client(hostname, port);

		// Start the client to connect to the server and send messages.
		client.run();
		scanner.close();
	}
}
