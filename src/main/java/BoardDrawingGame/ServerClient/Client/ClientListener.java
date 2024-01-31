package BoardDrawingGame.ServerClient.Client;


import java.util.List;

/**
 * A listener interface for handling disconnection events in a client-server communication scenario.
 * Implementations of this interface can define custom behavior for disconnect events.
 */
public interface ClientListener {

    /**
     * Handles the disconnection event.
     * Implementations of this method define the actions to be taken when the client disconnects from the server.
     */
    void handleDisconnect();

}
