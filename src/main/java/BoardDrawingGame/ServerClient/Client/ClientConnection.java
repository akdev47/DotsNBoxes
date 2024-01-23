package BoardDrawingGame.ServerClient.Client;



import BoardDrawingGame.ServerClient.SocketConnection;
import java.io.IOException;
import java.util.List;


public class ClientConnection extends SocketConnection {

    private AIClient AIClient;

    /**
     * Constructs a ClientConnection with the specified server address, port, and associated ChatClient.
     *
     * @param address    The IP address or hostname of the server.
     * @param port       The port number to connect to on the server.
     * @param AIClient The associated ChatClient instance.
     * @throws IOException If an I/O error occurs while establishing the connection.
     */
    protected ClientConnection(String address, int port, AIClient AIClient) throws IOException {
        super(address, port);
        super.start();
        this.AIClient = AIClient;
    }

    /**
     * Handles incoming messages received from the server.
     * Parses the message and notifies the associated ChatClient of chat messages.
     *
     * @param message The incoming message from the server.
     */
    @Override
    protected void handleMessage(String message) {

        String[] parts = message.split("~");

        if (parts.length == 3 && parts[0].equals("FROM")) {
            String username = parts[1];
            String chatMessage = parts[2];
            AIClient.recieveChatMessage(username, chatMessage);
        }
        
        if(parts.length == 1 && parts[0].equals("ALREADYLOGGEDIN")) {
            AIClient.recieveLogInStatus(false);
        } else if (parts.length == 1 && parts[0].equals("LOGIN")) {
            AIClient.recieveLogInStatus(true);
        } else if (parts[0].equals("LIST")) {
            AIClient.recieveListCommand(message);
        }
    }

    /**
     * Handles the event when the connection to the server is disconnected.
     * Notifies the associated ChatClient and closes the connection.
     */
    @Override
    protected void handleDisconnect() {
        System.out.println("Disconnected");
        AIClient.handleDisconnect();
        AIClient.close();
    }


    /**
     * Sends a chat message to the server.
     *
     * @param message The chat message to be sent.
     */
    public void sendChatMessage(String message) {

        String formattedMessage = "SAY~" + message;
        super.sendMessage(formattedMessage);
    }


    /**
     * Sends the list command to the server.
     *
     */
    public void sendList() {
        super.sendMessage("LIST");
    }

    /**
     * Sends the username to the server.
     *
     * @param username The username to be sent.
     */
    public void sendUsername(String username) {

        String formattedMessage = "LOGIN~" + username;
        super.sendMessage(formattedMessage);
    }

    /**
     * Closes the connection to the server.
     */
    public void close() {
        super.close();
    }

}

