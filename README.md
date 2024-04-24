# Client-Server Chat Application: User Manual with Commands

Welcome to the Client-Server Chat Application! This manual will guide you through the setup, use, and interaction with the application. This guide covers the following topics:
1. [System Requirements](#system-requirements)
2. [Application Components](#application-components)
3. [Installation and Setup](#installation-and-setup)
4. [Building the Project](#building-the-project)
5. [Starting the Server](#starting-the-server)
6. [Starting the Client](#starting-the-client)
7. [Sending and Receiving Messages](#sending-and-receiving-messages)
8. [Advanced Features](#advanced-features)
9. [Troubleshooting](#troubleshooting)

---

## System Requirements

Ensure your system meets the following requirements before installing and using the application:
- Java Development Kit (JDK) 11 or later
- Command-line interface (CLI) or terminal
- Internet connection (for network communication)

## Application Components

The chat application consists of the following components:

1. **Server**: Handles incoming client connections and routes messages.
   - `Server.java`
   - `RunServer.java`
   - `ConnectionPool.java`
   - `ChatServerHandler.java`

2. **Client**: Connects to the server and sends/receives messages.
   - `Client.java`
   - `RunClient.java`
   - `RequestFactory.java`

3. **Shell Scripts**: Simplifies the execution of server and client operations.
   - `build.sh`
   - `run_server.sh`
   - `run_client.sh`

## Installation and Setup

To get started with the application, follow these steps:

1. **Navigate to the Project Directory**
   - Open your command-line interface or terminal.
   - Use the `cd` command to navigate to the project directory where the source files are located:
     ```bash
     cd /path/to/project-directory
     ```

## Building the Project

1. **Build the Project**
   - Ensure you are in the project directory.
   - Run the `build.sh` script to compile the Java files:
     ```bash
     sh build.sh
     ```
   - If successful, the compiled files will be created in the same directory.

## Starting the Server

1. **Start the Server**
   - After building the project, start the server with the `run_server.sh` script:
     ```bash
     sh run_server.sh
     ```
   - By default, the server listens on port 42096. You can specify a different port when prompted.

2. **Stop the Server**
   - To stop the server, use `CTRL + C` in the terminal or simply close the terminal.

## Starting the Client

1. **Start the Client**
   - To start the client, use the `run_client.sh` script:
     ```bash
     sh run_client.sh
     ```
   - Provide the server's hostname (default: `localhost`) and port (default: `42096`).

2. **Stop the Client**
   - To exit the client, type `exit` and press `Enter` or use `CTRL + C`.

## Sending and Receiving Messages

1. **Sending Messages**
   - After starting the client, type your message and press `Enter`.
   - The message is sent to the server, then distributed to other connected clients.

2. **Receiving Messages**
   - Messages from other clients will be displayed in real-time in your terminal.

## Advanced Features

This section describes additional functionality that the application offers.

1. **Request Handling**
   - The `RequestFactory.java` class manages various types of requests like creating or joining groups.
   - Extend this class to add new request types if needed.

2. **Connection Pool**
   - The `ConnectionPool.java` class manages client connections and allows advanced communication features like grouping and topics.

## Troubleshooting

If you encounter issues, try the following steps:

1. **Connection Issues**
   - Ensure the server is running on the correct port.
   - Check your network connection.
   - Verify you are using the correct hostname and port when starting the client.

2. **Java Errors**
   - Ensure Java is properly installed and configured:
     ```bash
     java -version
     ```

3. **Unexpected Termination**
   - If the server or client terminates unexpectedly, check for error messages in the terminal.
   - Resolve any issues and restart the server/client as needed.

---

This manual provides a comprehensive guide to using the Client-Server Chat Application, including all necessary commands to build, start, and interact with the server and client. Follow these instructions, and refer to the troubleshooting section for any issues that arise.
