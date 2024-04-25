## Chat Client User Manual

Welcome to the chat client! This manual will guide you through the basic functionalities of the client and explain how to use the available commands.

### Connecting to the Server

1. **Launch the client application.**
2. **Enter the server hostname.**
    * This is usually "localhost" if the server is running on the same machine.
    * You can press enter to use the default hostname.
3. **Enter the server port number.**
    * The default port is 42069.
    * You can press enter to use the default port.

### Registering a Username

* Before you can send messages, you need to register a username.
* Use the following command to register:
    ```
    REGISTER <username>
    ```
    * Replace `<username>` with your desired username.

### Sending Messages

* Once you are registered, you can send messages to other users.

**Global Messages:**
* To send a message to all connected users, simply type your message and press enter.

**Targeted Messages:**
* To send a message to a specific user, use the following command:
    ```
    SEND <username> <message>
    ```
    * Replace `<username>` with the target user's username.
    * Replace `<message>` with the message you want to send.

* Alternatively, you can use the more specialized command:
```
SENDUSER <username>
```

**Group Messages:**
* To send a message to a specific group, use the following command:
    ```
    SEND <groupname> <message>
    ```
    * Replace `<groupname>` with the target group's name.
    * Replace `<message>` with the message you want to send.

* Alternatively, you can use a the more specialized command:
```
SENDGROUP <groupname>
```

### Creating and Joining Groups

* You can create and join groups to facilitate conversations with multiple users.

**Create a Group:**
    ```
    CREATE <groupname>
    ```

**Join a Group:**
    ```
    JOIN <groupname>
    ```

**Leave a Group:**
    ```
    LEAVE <groupname>
    ```

**Remove a Group:**
    ```
    REMOVE <groupname>
    ```

### Using Topics

* Topics allow you to subscribe to specific keywords and receive messages related to those keywords.

**Create a Topic:**
* Topics are automatically created when you use a hashtag (#) followed by a keyword in your message.

**Subscribe to a Topic:**
    ```
    SUBSCRIBE <topicname>
    ```

**Unsubscribe from a Topic:**
    ```
    UNSUBSCRIBE <topicname>
    ```

**List Available Topics:**
    ```
    TOPICS
    ```



### Exiting the Client

* To exit the client, type the following command and press enter:
    ```
    EXIT
    ```

### List online users
```
LIST
```

### List online users in a group
```
LISTGROUP <groupname>
```

### Additional Notes

* All commands are case-insensitive.
* You can use the up and down arrow keys to navigate through your command history.
* The server will automatically disconnect you if you are idle for too long.


***Enjoy your chat!***
