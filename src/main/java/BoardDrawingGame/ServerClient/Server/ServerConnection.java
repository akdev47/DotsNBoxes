package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.ServerClient.SocketConnection;
import BoardDrawingGame.ServerClient.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 * Represents a server-side connection handling for Dots And Boxes.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class ServerConnection extends SocketConnection {

    private ClientHandler clientHandler;
    private final BufferedReader reader;


    /**
     * Constructs a new ServerConnection instance.
     *
     * @param socket     The socket representing the connection.
     * @param chatServer The GameServer instance associated with this connection.
     * @throws IOException if an I/O error occurs when creating the BufferedReader.
     */
    protected ServerConnection(Socket socket,GameServer chatServer) throws IOException {
        super(socket);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientHandler = new ClientHandler(this,chatServer);
        setClientHandler(clientHandler);
    }


    /**
     * Handles incoming messages from clients.
     *
     * @param message The message received from the client.
     */
    @Override
    protected void handleMessage(String message) {

        try {
            if(message.contains(Protocol.SEPARATOR)) {
                String[] splitMessage = message.split(Protocol.SEPARATOR);

                if (splitMessage.length == 2) {
                    String firstCommand = splitMessage[0];

                    if (firstCommand.equals(Protocol.LOGIN)) {
                        getClientHandler().recieveUsername(splitMessage[1]);
                    } else if (firstCommand.equals(Protocol.MOVE)) {
                        getClientHandler().recieveMove(splitMessage[1]);
                    } else if (firstCommand.equals(Protocol.HELLO)) {
                        getClientHandler().recieveHello(splitMessage[1]);
                    }
                }

            }
            else {
                if(message.equals(Protocol.LIST)) {
                    getClientHandler().recieveListCommand();
                }
                else if(message.equals(Protocol.QUEUE)) {
                    getClientHandler().receiveQueueCommand();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles disconnection of the client via the Client Handler.
     */
    @Override
    protected void handleDisconnect() {
        getClientHandler().handleDisconnect();
    }

    /**
     * Starts the server connection, listening for messages from clients.
     */
    public void run() {
        try {
            while (true) {
                String message = this.reader.readLine();
                if (message == null) {
                    break;
                }
                handleMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handleDisconnect();
        }
    }


    /**
     * Sets the client handler associated with this connection.
     *
     * @param clientHandler The client handler to set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }


    /**
     * Retrieves the client handler associated with this connection.
     *
     * @return The client handler associated with this connection.
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }


    /**
     * Sends a login message to the client.
     */
    public void sendLogIn() {
        System.out.println("LOGIN");
        super.sendMessage(Protocol.LOGIN);
    }

    /**
     * Sends a hello message to the client.
     */
    public void sendHello() {
        String formattedMessage = Protocol.HELLO + Protocol.SEPARATOR + "Server created by AK.";
        super.sendMessage(formattedMessage);
    }


    /**
     * Sends a move message to the client.
     *
     * @param move The move to send to the client.
     */
    public void sendMove(String move) {
        System.out.println("Sending move to clients: " + move);
        String formattedMessage = Protocol.MOVE + Protocol.SEPARATOR + move;

        super.sendMessage(formattedMessage);
    }

    /**
     * Sends an "already logged in" message to the client.
     */
    public void sendAlreadyLoggedIn() {
        System.out.println("ALREADY LOGGED IN");
        super.sendMessage(Protocol.ALREADYLOGGEDIN);
    }

    /**
     * Sends a list message to the client.
     *
     * @param formattedList The formatted list to send to the client.
     */
    public void sendList(String formattedList) {
        System.out.println("LIST");
        super.sendMessage(formattedList);
    }


    /**
     * Sends a game over message to the client.
     *
     * @param reason The reason for the game over.
     */
    public void sendGameOver(String reason) {
        String formattedMessage = Protocol.GAMEOVER + Protocol.SEPARATOR + reason;
        super.sendMessage(formattedMessage);
    }


    /**
     * Sends a game connection status message to the client.
     *
     * @param player1Name The name of player 1.
     * @param player2Name The name of player 2.
     */
    public void sendGameConnectionStatus(String player1Name, String player2Name) {
        String formattedMessage = Protocol.NEWGAME + Protocol.SEPARATOR + player1Name + Protocol.SEPARATOR + player2Name;
        super.sendMessage(formattedMessage);
    }


}
