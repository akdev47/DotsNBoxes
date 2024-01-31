package BoardDrawingGame.ServerClient.Client;



import BoardDrawingGame.ServerClient.SocketConnection;
import BoardDrawingGame.ServerClient.Protocol;
import java.io.IOException;


public class ClientConnection extends SocketConnection {

    private final Client client;

    /**
     * Constructs a ClientConnection with the specified server address, port, and associated ChatClient.
     *
     * @param address    The IP address or hostname of the server.
     * @param port       The port number to connect to on the server.
     * @param AIClient The associated ChatClient instance.
     * @throws IOException If an I/O error occurs while establishing the connection.
     */
    protected ClientConnection(String address, int port, Client AIClient) throws IOException {
        super(address, port);
        super.start();
        this.client = AIClient;
    }

    /**
     * Handles incoming messages received from the server.
     * Parses the message and notifies the associated client of the protocol command.
     *
     * @param message The incoming message in appliance of the protocol from the server..
     */
    @Override
    protected synchronized void handleMessage(String message) {

        String[] parts = message.split("~");

        if(parts.length== 3 && parts[0].equals(Protocol.NEWGAME)) {
            String player1Name = parts[1];
            String player2Name = parts[2];
            client.recieveNewGameCommand(player1Name, player2Name);
        }

        if(parts.length== 3 && parts[0].equals(Protocol.GAMEOVER)) {
            String reason = parts[1];
            String winnerName = parts[2];
            client.recieveGameOverCommand(reason, winnerName);
        }


        if(parts.length== 2 && parts[0].equals(Protocol.MOVE)) {
            System.out.println("Client is recieveing move");
            String move = parts[1];
            client.recieveMove(move);
        }

        if(parts.length==2 && parts[0].equals(Protocol.HELLO)) {
            String serverDescription = parts[1];
            client.reieveHelloCommand(serverDescription);
        }


        if(parts.length == 1 && parts[0].equals(Protocol.ALREADYLOGGEDIN)) {
            client.recieveLogInStatus(false);
        } else if (parts.length == 1 && parts[0].equals(Protocol.LOGIN)) {
            client.recieveLogInStatus(true);
        } else if (parts[0].equals("LIST")) {
            client.recieveListCommand(message);
        }
    }

    /**
     * Handles the event when the connection to the server is disconnected.
     * Notifies the associated client and closes the connection.
     */
    @Override
    protected void handleDisconnect() {
        System.out.println("Disconnected");
        client.handleDisconnect();
        client.close();
    }


    /**
     * Sends a hello command to the server.
     *
     */
    public void sendHello(String description) {
        String formattedMessage = "HELLO~" + description;
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
     * Sends the queue command to the server.
     *
     */
    public void sendQueue() {
        super.sendMessage("QUEUE");
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
     * Sends the move to the server.
     *
     * @param move The move to be sent.
     */
    public void sendMove(int move) {
        String formattedMessage = "MOVE~" + move;
        super.sendMessage(formattedMessage);
    }

    /**
     * Closes the connection to the server.
     */
    public void close() {
        super.close();
    }

}

