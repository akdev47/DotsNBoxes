package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.logic.Game;
import BoardDrawingGame.view.SquaresToDrawForUI;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * The GameServer class is responsible for creating and managing games and
 * represents the server for the Dots And Boxes Game.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class GameServer extends SocketServer {

        private final List<ClientHandler> clients;

        private final List<ClientHandler> queueList;

    Map<Game,List<ClientHandler>> gamesMap = new HashMap<>();
    

    /**
     * Constructs a new GameSever to play Dots And Boxes.
     * @param port the port to listen on
     * @throws IOException if the server socket cannot be created, for example, because the port is already bound.
     */
    public GameServer(int port) throws IOException {
        super(port);
        clients = new ArrayList<>();
        queueList = new ArrayList<>();
    }
    /**
     * Returns the port on which this server is listening for connections.
     *
     * @return the port on which this server is listening for connections
     */
    @Override
    public int getPort() {
        return super.getPort();
    }

    /**
     * Accepts connections and starts a new thread for each connection.
     * This method will block until the server socket is closed, for example by invoking closeServerSocket.
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    @Override
    public void acceptConnections() throws IOException {
        super.acceptConnections();
    }

    /**
     * Closes the server socket. This will cause the server to stop accepting new connections.
     * If called from a different thread than the one running acceptConnections, then that thread will return from
     * acceptConnections.
     */
    @Override
    public synchronized void close() {
        super.close();
    }

    /**
     * Creates a new connection handler for the given socket.
     *
     * @param socket the socket for the connection
     * @return the connection handler
     */
    @Override
    protected void handleConnection(Socket socket) throws IOException {
        // TODO: Create a new ClientHandler and ServerConnection and start the connection.
        ServerConnection serverConnection = new ServerConnection(socket,this);
        ClientHandler clientHandler = new ClientHandler(serverConnection,this);

        new Thread(serverConnection::run).start();

    }

    public static void main(String[] args) {



        while(true) {

            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter port number for the server: ");
                int port = scanner.nextInt();
                GameServer chatServer = new GameServer(port);
                System.out.println("Server started on port: " + chatServer.getPort());
                chatServer.acceptConnections();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal Port Number.");
            } catch (InputMismatchException e) {
                System.out.println("Dont enter a string");
            }
        }



    }

    /**
     * Adds the received client to the client list. Client enters the registered players list.
     *
     * @param client The client that will be added to the client list.
     */
    public synchronized  void addClient(ClientHandler client) {
        System.out.println("Recieved username: " + client.getUserName());
        clients.add(client);
    }


    /**
     * Removes the received client to the client list. Client exits the registered players list.
     *
     * @param client The client that will be removed to the client list.
     */
    public synchronized  void removeClient(ClientHandler client) {
        System.out.println(client.getUserName()+ " disconnected");
        clients.remove(client);
    }





    /**
     * Sends a game over message to all clients in the specified game when a client disconnects.
     * Removes the game from the gamesMap and the disconnected client from the clients list.
     *
     * @param game   The game that is over due to a client disconnection.
     * @param client The client handler of the disconnected client.
     */
    public void sendGameOverDisconnect(Game game,ClientHandler client) {
        if(gamesMap.get(game) != null) {
            System.out.println("Game is over. " + client.getUserName() + " disconnected.");

            List<ClientHandler> clientsToSendGameOver = gamesMap.get(game);

            String gameWinner;

            if (clientsToSendGameOver.get(0).getUserName().equals(client.getUserName())) {
                gameWinner = clientsToSendGameOver.get(1).getUserName();
            } else {
                gameWinner = clientsToSendGameOver.get(0).getUserName();
            }



            for (ClientHandler c : clientsToSendGameOver) {
                //TODO: add a for loop to sendMove to all clients.
                c.sendGameOver("DISCONNECT~" + gameWinner);
            }
            gamesMap.remove(game);
            clients.remove(client);
        }
    }


    /**
     * Sends a game over message to all clients in the specified game when the game is over.
     * Removes the game from the gamesMap.
     *
     * @param game The game that is over.
     */
    public void sendGameOver(Game game) {
        System.out.println("Game is over. ");

        List<ClientHandler> clientsToSendGameOver= gamesMap.get(game);

        System.out.println("Score Player 1: " + game.getScorePlayer1());
        System.out.println("Score Player 2: " + game.getScorePlayer2());

        String gameWinner;

        if(game.getScorePlayer1()>game.getScorePlayer2()) {
            gameWinner = clientsToSendGameOver.get(0).getUserName();
        }
        else {
            gameWinner = clientsToSendGameOver.get(1).getUserName();
        }
        for(ClientHandler c : clientsToSendGameOver) {
            //sends a game over to all clients in the current game with the reason of gameover.
            c.sendGameOver("VICTORY~" + gameWinner);
        }
        gamesMap.remove(game);
    }


    /**
     * Handles the current move of a client in a game.
     * Sends the move to all clients in the game and checks if the game is finished after the move.
     *
     * @param client The client handler of the player making the move.
     * @param move   The move made by the client.
     * @param game   The game in which the move is made.
     */
    public void handleCurrentMove(ClientHandler client, String move, Game game) {


        if(game.getUtility().isGameFinished()) {
           sendGameOver(game);

        }
        else {
            System.out.println("Recieved move from: " + client.getUserName() + ": " + move);
            List<ClientHandler> clientsToSendMoveTo = gamesMap.get(game);

            SquaresToDrawForUI moveInfo = game.getUtility().getSquaresToDrawForUI(game.getUtility().getServerIndexToLine(
                    Integer.parseInt(move)));

            int movePoint = moveInfo.getCurrentMovePoint();

            if(movePoint>0) {
                game.incrementPointByName(client.getUserName(),movePoint);
            }

            game.getBoard().markLine(Integer.parseInt(move));


            for(ClientHandler c : clientsToSendMoveTo) {
                // sends the current move to all connected clients.
                c.sendMove(move);
            }

            if(game.getUtility().isGameFinished()) {
                sendGameOver(game);

            }
        }



    }

    /**
     * Returns the list of clients connected to the server.
     *
     * @return The list of clients.
     */
    public List<ClientHandler> getClients() {
        return clients;
    }

    /**
     * Checks if a username is already taken by a client connected to the server.
     *
     * @param username The username to check.
     * @return true if the username is already taken, false otherwise.
     */
    public synchronized boolean containsUsername(String username) {
        System.out.println(clients.size());
        if(clients.isEmpty()) {
            return false;
        }
        else {
            for(ClientHandler c : clients) {
                if(c.getUserName().equals(username)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the list of clients in the queue waiting to start a game.
     *
     * @return The list of clients in the queue.
     */
    public List<ClientHandler> getQueueOfClients() {
        return queueList;
    }

    public void startGameConnection() {
        synchronized (queueList) {
            List<ClientHandler> result = new ArrayList<>();
            if (getQueueOfClients().size() >= 2) {
                result.add(getQueueOfClients().get(0));
                result.add(getQueueOfClients().get(1));
                queueList.remove(0);
                queueList.remove(0);
                startGame(result);
            }
        }
    }


    /**
     * Retrieves the game associated with the specified username.
     * Searches through the gamesMap to find a game where the username matches either player1Name or player2Name.
     *
     * @param username The username of the player.
     * @return The game associated with the username, or null if no game is found.
     */
    public Game getGameWithUsername(String username) {
        for (Game game : gamesMap.keySet()) {
            if(game.getPlayer1Name().equals(username) || game.getPlayer2Name().equals(username)) {
                return game;
            }
        }
        return null;

    }

    /**
     * Starts a new game with the specified list of clients.
     * Creates a new instance of Game, sets the player names,
     * adds the game to the gamesMap, and notifies the clients about the game connection status.
     *
     * @param clientList The list of client handlers participating in the game.
     */
    public synchronized void startGame(List<ClientHandler> clientList){
        Game game = new Game();


        String player1Name = clientList.get(0).getUserName();
        String player2Name = clientList.get(1).getUserName();

        game.setPlayerNames(player1Name,player2Name);

        gamesMap.put(game,clientList);

        for(ClientHandler c : clientList) {
            c.sendGameConnectionStatus(player1Name,player2Name);
        }


    }


}


