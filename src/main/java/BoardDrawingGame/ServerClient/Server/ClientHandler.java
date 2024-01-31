package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.ServerClient.Protocol;
import BoardDrawingGame.logic.Game;
import java.util.List;

/**
 * Represents a handler for individual clients connected to the server.
 * This class manages interactions with clients, such as receiving messages,
 * handling moves, managing usernames, and sending game-related information.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class ClientHandler {

    private final ServerConnection serverConnection;
    private final GameServer gameServer;

    private String userName;


    public ClientHandler(ServerConnection serverConnection,GameServer gameServer) {
        this.serverConnection = serverConnection;
        this.gameServer = gameServer;
        serverConnection.setClientHandler(this);
    }



    /**
     * Recieves username.
     *
     * @param username The username thats received.
     */
    public synchronized void recieveUsername(String username) {
        if(gameServer.containsUsername(username)) {
            serverConnection.sendAlreadyLoggedIn();
        }
        else {
            boolean userNameSet = true;
            this.userName = username;
            serverConnection.sendLogIn();
            gameServer.addClient(this);
        }

    }

    /**
     * Recieves list command .
     *
     *
     */
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

    public synchronized void receiveQueueCommand() {
        List<ClientHandler> currentClientList = gameServer.getQueueOfClients();

        if (currentClientList.isEmpty()) {
            currentClientList.add(this);
            System.out.println("First Player is in queue");
        }
        else {
            if (currentClientList.contains(this)) {
                System.out.println("Allright, " + this.getUserName() + " out of queue :(");
                currentClientList.remove(this);
            }
            else {
                currentClientList.add(this);
                System.out.println(this.getUserName() + " is in queue :)");
                gameServer.startGameConnection();
            }
        }

        for (ClientHandler c : currentClientList) {
            System.out.print(c.getUserName() + " ");
        }
        System.out.println();
    }



    /**
     * Recieves a move.
     *
     * @param move The move thats recieved.
     */
    public void recieveMove(String move) {
        Game currentGame = gameServer.getGameWithUsername(this.getUserName());
        if(currentGame!= null) {
            gameServer.handleCurrentMove(this,move,currentGame);
        }

    }
    /**
     * Recieves a hello command.
     *
     * @param description The description of the connected server.
     */
    public void recieveHello(String description) {
        System.out.println(Protocol.HELLO + Protocol.SEPARATOR + description);
        serverConnection.sendHello();
    }

    /**
     * Handles disconnect and removes client.
     *
     */
    public void handleDisconnect() {
        //        System.out.println("Disconnected");
        Game currentGame = gameServer.getGameWithUsername(this.getUserName());
        gameServer.removeClient(this);
        gameServer.sendGameOverDisconnect(currentGame,this);
    }
    /**
     * Gets the username of the client.
     * @return String username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sends a move to the client using the server connection.
     *
     * @param move The move to be sent.
     */
    public void sendMove(String move) {
        serverConnection.sendMove(move);
    }

    /**
     * Sends a game over message to the client using the server connection.
     *
     * @param reason The reason for the game over.
     */
    public void sendGameOver(String reason) {
        serverConnection.sendGameOver(reason);
    }

    /**
     * Sends the game connection status to the client using the server connection.
     *
     * @param player1Name The username of player 1.
     * @param player2Name The username of player 2.
     */
    public void sendGameConnectionStatus(String player1Name, String player2Name) {
        serverConnection.sendGameConnectionStatus(player1Name,player2Name);
    }


}
