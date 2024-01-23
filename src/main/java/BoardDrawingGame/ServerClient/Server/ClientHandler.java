package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.ServerClient.protocol.Protocol;
import java.util.List;

public class ClientHandler {

    private ServerConnection serverConnection;
    private GameServer gameServer;

    private String userName;

    private boolean userNameSet = false;

    public ClientHandler(ServerConnection serverConnection,GameServer gameServer) {
        this.serverConnection = serverConnection;
        this.gameServer = gameServer;
        serverConnection.setClientHandler(this);
    }



    /**
     * Recieves username
     *
     * @param username The username thats received
     */
    public synchronized void recieveUsername(String username) {
        if(gameServer.containsUsername(username)) {
            serverConnection.sendAlreadyLoggedIn();
        }
        else {
            userNameSet = true;
            this.userName = username;
            serverConnection.sendLogIn();
            gameServer.addClient(this);
        }

    }

    public void recieveListCommand() {
        String formattedList = Protocol.LIST;
//        List<ClientHandler> clients = gameServer.getClients();
//        System.out.print("LIST");
//        for(ClientHandler h : clients) {
//            System.out.print(Protocol.SEPARATOR + h.getUserName());
//        }
//        System.out.println(" ");

                List<ClientHandler> clients = gameServer.getClients();

                for(ClientHandler h : clients) {
                    formattedList = formattedList.concat(Protocol.SEPARATOR + h.getUserName());
                }
        serverConnection.sendList(formattedList);
    }

    /**
     * Recieves chat messages.
     *
     * @param message The message thats received
     */
    public void recieveChatMessage(String message) {
        //        System.out.println("Recieved chat message from: " + userName + ": " + message);
        gameServer.handleChatMessage(this, message);
    }

    /**
     * Handles disconnect and removes client.
     *
     */
    public void handleDisconnect() {
        //        System.out.println("Disconnected");
        gameServer.removeClient(this);
    }
    /**
     * Gets the username of the client.
     * @return String username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the username of the client.
     * @return boolean if the username is set.
     */
    public boolean getUserNameSet() {
        return userNameSet;
    }

    /**
     * Sends a chat message using server connection, by sending the username and message.
     * @param userName Username of the client
     * @param message Message that the client will send.
     */
    public void sendChatMessage(String userName,String message) {
        serverConnection.sendChatMessage(userName,message);
    }


}
