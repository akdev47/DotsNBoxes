package BoardDrawingGame.ServerClient.Client;


import BoardDrawingGame.Entities.Player;
import BoardDrawingGame.ServerClient.Protocol;
import BoardDrawingGame.logic.AIPlayer;
import BoardDrawingGame.view.ClientListenerGUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.scene.paint.Color;

/**
 * Represents a client that connects to the game server.
 * This class has methods for the client to interact with the server
 * and recieve information from the server.
 *
 * @author Ali Kaan Uysal
 * @author Bas Van Dijk
 */
public class Client {
    private ClientConnection clientConnection;
    private String username;

    private AIPlayer AIPlayer;

    private Player humanPlayer;
    private boolean logInSuccessful;

    private Color playerColor;



    private boolean gameOver = false;
    private boolean isPlayerTurn = false;
    private boolean gameStarted = false;

    private List<ClientListener> listeners;

    private ClientListenerGUI guiListener;

    private boolean inQueue = false;

    private String currentList;

    private int currentGamePlayer1Score;

    private int currentGamePlayer2Score;

    private String currentGamePlayer1Name, currentGamePlayer2Name;


    /**
     * Constructs a new Client object with the server address and port.
     * Sets the listeners and creates a client connection.
     *
     * @param address The IP address or hostname of the server.
     * @param port    The port number to connect to on the server.
     * @throws IOException If an I/O error occurs while establishing the connection.
     */

    public Client(String address, int port) throws IOException {
        clientConnection = new ClientConnection(address, port,this);
        listeners = new ArrayList<>();
    }


    /**
     * Sets the GUI listener for handling client events.
     *
     * @param listener The GUI listener to set.
     */
    public void setGUIListener(ClientListenerGUI listener) {
        this.guiListener = listener;
    }

    /**
     * Sets the AI player for the client.
     * This method initializes the AI player and sets the human player to null.
     */
    public void setAIPlayer() {
        humanPlayer = null;
        AIPlayer = new AIPlayer();
    }

    /**
     * Sets the human player for the client.
     * This method initializes the human player and sets the AI player to null.
     */
    public void setHumanPlayer() {
        AIPlayer = null;
        humanPlayer = new Player();
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
     * Sends a "Hello" message to the server.
     * This method sends a formatted "Hello" message to the server containing the client description.
     *
     * @param description The description of the client.
     */
    public void sendHello(String description) {
        String formattedDescription = "Client by " + description;
        clientConnection.sendHello(formattedDescription);
    }

    /**
     * Sends a "List" command to the server.
     * This method requests the server to send a list of available players.
     */
    public void sendList() {

        clientConnection.sendList();
    }

    /**
     * Sends a "Queue" command to the server.
     * This method requests the server to put the client in the game queue.
     */
    public void sendQueue() {
        clientConnection.sendQueue();
    }

    /**
     * Sends a move to the server.
     * This method sends the specified move to the server.
     *
     * @param move The move to be sent.
     */
    public void sendMove(int move) {
        clientConnection.sendMove(move);
    }

    /**
     * Returns the username of the client.
     *
     * @return The username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the client.
     *
     * @param username The username to be set.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Returns whether the login was successful.
     *
     * @return True if the login was successful, false otherwise.
     */
    public boolean getLogInSuccesfull() {
        return logInSuccessful;
    }


    /**
     * Returns whether the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean getGameover() {
        return gameOver;
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
     * Receives a "Hello" command from the server.
     * This method prints the server description received as part of the "Hello" command.
     *
     * @param serverDescription The description received from the server.
     */
    public void reieveHelloCommand(String serverDescription) {
        System.out.println(Protocol.HELLO + Protocol.SEPARATOR + serverDescription);
    }

    /**
     * Receives a "List" command from the server.
     * This method prints the list of available players received from the server.
     *
     * @param list The list of available players received from the server.
     */
    public void recieveListCommand(String list) {
        System.out.println("\n"+ list);
        currentList = list;
    }

    /**
     * Receives a "NewGame" command from the server.
     * This method handles the initialization of a new game based on the received player names.
     *
     * @param player1Name The name of Player 1.
     * @param player2Name The name of Player 2.
     */
    public synchronized void recieveNewGameCommand(String player1Name, String player2Name)  {
        System.out.println("Successfully created a game.");
        System.out.println("Player 1 Name: " + player1Name);
        System.out.println("Player 2 Name: " + player2Name);


        currentGamePlayer1Score = 0;
        currentGamePlayer2Score = 0;

        gameStarted = true;

        System.out.println("Test for username: " + getUsername());
        if(player1Name.equals(getUsername())) {
            System.out.println("It is this players turn.");
            setIsPlayerTurn(true);
            setPlayerColor(Color.RED);
            this.currentGamePlayer1Name = player1Name;
        }
        else {
            setIsPlayerTurn(false);
            setPlayerColor(Color.BLUE);
            this.currentGamePlayer2Name = player2Name;
        }


        if (guiListener != null) {
            guiListener.handleNewGame(player1Name, player2Name);
        }

    }


    /**
     * Sets whether it is currently the player's turn.
     *
     * @param isPlayerTurn A boolean indicating whether it is the player's turn.
     */
    public void setIsPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    /**
     * Returns whether it is currently the player's turn.
     *
     * @return A boolean indicating whether it is the player's turn.
     */
    public boolean getPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Returns whether the game has started.
     *
     * @return A boolean indicating whether the game has started.
     */
    public boolean getGameStarted() {
        return gameStarted;
    }

    /**
     * Returns whether the client is currently in a queue.
     *
     * @return A boolean indicating whether the client is in a queue.
     */
    public boolean getInQueue() {
        return inQueue;
    }

    /**
     * Sets whether the client is currently in a queue.
     *
     * @param value A boolean indicating whether the client is in a queue.
     */
    public void setInQueue(boolean value) {
       inQueue = value;
    }

    /**
     * Sets whether the game has started.
     *
     * @param gameStarted A boolean indicating whether the game has started.
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }



    /**
     * Returns the AI player associated with the client.
     *
     * @return The AI player associated with the client.
     */
    public BoardDrawingGame.logic.AIPlayer getAIPlayer() {
        return AIPlayer;
    }

    /**
     * Returns the human player associated with the client.
     *
     * @return The human player associated with the client.
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Receives the login status from the server.
     *
     * @param value A boolean indicating the login status recieved from the server.
     */
    public void recieveLogInStatus(boolean value) {
        if(value) {
            logInSuccessful = true;
        }
        else {
            logInSuccessful = false;
        }
    }

    /**
     * Receives a move from the server.
     *
     * @param move The move received from the server.
     */
    public synchronized void recieveMove(String move) {



        System.out.println("Recieved move: " + move);


        if (guiListener != null) {
            guiListener.handleMove(move);
        }


        if(AIPlayer != null) {
            if (getPlayerTurn() && getAIPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) == 4) {
                setIsPlayerTurn(true);
            } else if (getPlayerTurn() && getAIPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) != 4) {
                setIsPlayerTurn(false);
            } else if (!getPlayerTurn() && getAIPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) != 4) {
                setIsPlayerTurn(true);
            } else if (!getPlayerTurn() && getAIPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) == 4) {
                setIsPlayerTurn(false);
            }

            getAIPlayer().getBoard().markLine(Integer.parseInt(move));
        } else if(humanPlayer != null) {
            if (getPlayerTurn() && getHumanPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) == 4) {
                setIsPlayerTurn(true);
            } else if (getPlayerTurn() && getHumanPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) != 4) {
                setIsPlayerTurn(false);
            } else if (!getPlayerTurn() && getHumanPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) != 4) {
                setIsPlayerTurn(true);
            } else if (!getPlayerTurn() && getHumanPlayer().getUtility()
                    .calculatePointWithServerIndex(Integer.parseInt(move)) == 4) {
                setIsPlayerTurn(false);
            }


            getHumanPlayer().getBoard().markLine(Integer.parseInt(move));

        }

        if (guiListener != null) {
            guiListener.handleInfoText();
        }

        if(getAIPlayer()!=null) {
            guiListener.callAIMoveForServer();
        }




    }
    /**
     * Receives a game over command from the server.
     *
     * @param reason The reason for the game over.
     * @param winner The winner of the game.
     */
    public synchronized void recieveGameOverCommand(String reason,String winner ) {
        System.out.println("Game Over! " + winner + " won. Reason: " + reason);


        gameOver = true;
        setIsPlayerTurn(false);
        setInQueue(false);
        currentGamePlayer1Name = "";
        currentGamePlayer2Name = "";

        if(guiListener!=null) {
            guiListener.handleGameOver(reason,winner);
        }
    }

    /**
     * Exits the game, setting gameOver to be false.
     */
    public void exitGame() {
        gameOver = false;
    }

    /**
     * Notifies all listeners when the client disconnects from the server.
     */
    public void handleDisconnect() {
        for (ClientListener listener : listeners) {
            listener.handleDisconnect();
        }

    }

    /**
     * Returns the current list which is a String.
     *
     * @return The current list.
     */
    public String getCurrentList() {
        return currentList;
    }

    /**
     * Returns the color of the current player.
     *
     * @return The color of the current player.
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Returns the color of the other player based on the current player's color.
     *
     * @return The color of the other player.
     */
    public Color getOtherPlayerColor() {
        if (playerColor == Color.RED) {
            return Color.BLUE;
        }
        else {
            return Color.RED;
        }
    }

    /**
     * Sets the color of the player.
     *
     * @param color The color to set for the player.
     */
    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }


    /**
     * Returns the current score of player 1 in the ongoing game.
     *
     * @return The current score of player 1.
     */
    public int getCurrentGamePlayer1Score() {
        return currentGamePlayer1Score;
    }


    /**
     * Returns the current score of player 2 in the ongoing game.
     *
     * @return The current score of player 2.
     */
    public int getCurrentGamePlayer2Score() {
        return currentGamePlayer2Score;
    }

    /**
     * Increments the score of the specified player by the given points.
     *
     * @param playerNum The number of the player whose score needs to be incremented (1 or 2).
     * @param points    The points to add to the player's score.
     */
    public void incrementPlayerScore(int playerNum, int points) {
        if(playerNum==1) {
            currentGamePlayer1Score += points;
        } else {
            currentGamePlayer2Score += points;
        }
    }
}
