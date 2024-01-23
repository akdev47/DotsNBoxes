package BoardDrawingGame.ServerClient.Client;


import BoardDrawingGame.ServerClient.Server.ClientHandler;
import BoardDrawingGame.ServerClient.Server.GameServer;
import BoardDrawingGame.ServerClient.protocol.Protocol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AIClient {

    private ClientConnection clientConnection;
    private String username;

    private boolean logInSuccessful;
    private List<ClientListener> listeners;


    /**
     * Constructs a ChatClient with the specified server address, port, and username.
     *
     * @param address  The IP address or hostname of the server.
     * @param port     The port number to connect to on the server.
     * @param username The username to be used by the client.
     * @throws IOException If an I/O error occurs while establishing the connection.
     */
    public AIClient(String address, int port,String username) throws IOException {
        clientConnection = new ClientConnection(address, port,this);
        this.username = username;
        listeners = new ArrayList<>();
    }

    public AIClient(String address, int port) throws IOException {
        clientConnection = new ClientConnection(address, port,this);
        listeners = new ArrayList<>();
    }

    /**
     * Closes the client connection.
     */
    public void close() {
        clientConnection.close();
    }

    /**
     * Sends the specified username to the server.
     *
     * @param username The username to be sent.
     */
    public void sendUsername(String username) {
        clientConnection.sendUsername(username);
    }

    /**
     * Sends a chat message to the server.
     *
     * @param message The chat message to be sent.
     */
    public void sendChatMessage(String message) {

        clientConnection.sendChatMessage(message);
    }

    public void sendList() {

        clientConnection.sendList();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getLogInSuccesfull() {
        return logInSuccessful;
    }



    /**
     * Adds a client listener to the list of listeners.
     *
     * @param listener The client listener to be added.
     */
    public synchronized void addListener(ClientListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a client listener from the list of listeners.
     *
     * @param listener The client listener to be removed.
     */
    public synchronized void removeListener(ClientListener listener) {
        listeners.remove(listener);
    }


    /**
     * Receives a chat message from the server and notifies all listeners.
     *
     * @param senderUsername The username of the message sender.
     * @param message        The received chat message.
     */
    public void recieveChatMessage(String senderUsername, String message) {
        for (ClientListener listener : listeners) {
            listener.receiveChatMessage(senderUsername, message);
        }
    }

    public void recieveListCommand(String List) {
        System.out.println("\n"+ List);
    }





    public void recieveLogInStatus(boolean value) {
        if(value) {
            logInSuccessful = true;
        }
        else {
            logInSuccessful = false;
        }
    }

    /**
     * Notifies all listeners when the client disconnects from the server.
     */
    public void handleDisconnect() {
        for (ClientListener listener : listeners) {
            listener.handleDisconnect();
        }
    }


}
