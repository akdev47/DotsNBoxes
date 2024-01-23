package BoardDrawingGame.ServerClient.Client;


import java.util.List;

public interface ClientListener {
    void receiveChatMessage(String senderUsername, String message);
    void handleDisconnect();

}
