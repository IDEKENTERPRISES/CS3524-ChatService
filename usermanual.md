## User Manual for Chat Client

This manual will guide you through the functionalities of the chat client. 

### Getting Started

1. **Run the Client:**
    - Open a terminal window.
    - Navigate to the directory containing the `RunClient.java` file.
    - Execute the command: `java RunClient.java`
2. **Connect to Server:**
    - You will be prompted to enter the server hostname (default is localhost) and port number (default is 42096).
    - If you are connecting to a server on your local machine, you can leave the defaults.
    - If connecting to a remote server, enter the correct hostname and port number.

### Basic Commands

Once connected, you can use the following commands:

* **`REGISTER <username>`:** Register with a unique username.
* **`UNREGISTER`:** Unregister from the server. 
* **`SEND <username> <message>`:** Send a private message to a specific user.
* **`SEND <groupname> <message>`:** Send a message to a specific group.
* **`<message>`:** Send a message to the global chat. 
* **`CREATE <groupname>`:** Create a new group.
* **`JOIN <groupname>`:** Join an existing group.
* **`LEAVE <groupname>`:** Leave a group. 
* **`REMOVE <groupname>`:** Remove a group (only if you created it and it has no members).
* **`TOPIC <topicname>`:** Create a new topic.
* **`SUBSCRIBE <topicname>`:** Subscribe to a topic to receive messages containing the topic name.
* **`UNSUBSCRIBE <topicname>`:** Unsubscribe from a topic.
* **`TOPICS`:** List all available topics.
* **`#<topicname>`:** Include a topic name in your message with a hashtag to send it to subscribers of that topic.

### Additional Notes:

* Messages containing topic names (with hashtags) will be sent to both the global chat and the subscribers of that topic. 
* You can include multiple topic names in a single message.
* Use the `exit` command or press `Ctrl+C` to exit the client.

## Enjoy chatting!
