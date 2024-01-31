package BoardDrawingGame.ServerClient.Client;









import BoardDrawingGame.ServerClient.Protocol;
import BoardDrawingGame.view.Line;
import java.io.IOException;
import java.util.Scanner;

public class PlayerClientTUI implements ClientListener{


    public static void main(String[] args) {
        PlayerClientTUI playerClientTUI = new PlayerClientTUI();
        playerClientTUI.runTUI();
    }

    /**
     * Runs the Text User Interface for the chat client.
     * It prompts the user for server address, port, and username, and allows the user
     * to send chat messages until they decide to quit.
     */
    public void runTUI() {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter server address: ");
            String serverAddress = scanner.nextLine();
            System.out.print("Enter server port: ");
            int serverPort = scanner.nextInt();
            scanner.nextLine();


            Client client = new Client(serverAddress, serverPort);
            client.setHumanPlayer();
            client.addListener(this);

            String username = "";

            while (!client.getLogInSuccesfull()) {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                client.sendUsername(username);
                Thread.sleep(500);
            }
            client.setUsername(username);
            System.out.println("Username: " + client.getUsername());




            while (true) {
                Thread.sleep(500);
                while (!client.getGameStarted() || !client.getInQueue()) {
                    //                    System.out.println("Is player in queue: " + playerInQueue);
                    Thread.sleep(1000);

                    String message = "";
                    if (!client.getInQueue()) {
                        System.out.print("Enter a command message (or type 'quit' to exit): ");
                        message = scanner.nextLine();
                    }

                    if ("quit".equalsIgnoreCase(message)) {
                        client.close();
                        break;
                    }

                    if (message.equalsIgnoreCase(Protocol.LIST)) {
                        client.sendList();
                    } else if (message.equalsIgnoreCase(Protocol.QUEUE)) {
                        client.setInQueue(true);
                        client.sendQueue();
                    }
                }
                //
                //                    System.out.println("Goes into here. 1");





                while (!client.getGameover()) {
                    Thread.sleep(500);
                    while (!client.getPlayerTurn()) {
                        Thread.sleep(1000);
                        if(client.getGameover()) {
                            break;
                        }
                    }


                    if (client.getPlayerTurn() && !client.getGameover()) {

                        boolean validMove = false;
                        int moveToSendToServer = 0;
                        while (!validMove) {
                            System.out.print("Enter a valid move [0,59] ");
                            moveToSendToServer = scanner.nextInt();
                            
                            Line currentLine = client.getHumanPlayer().getUtility().getServerIndexToLine(moveToSendToServer);
                            if(client.getHumanPlayer().getBoard().isValidMove(currentLine)) {
                                validMove = true;
                            }
                        }
                        
                            client.sendMove(moveToSendToServer);

                    }

                }
                System.out.println("Game Over");
                client.setInQueue(false);
                client.setGameStarted(false);
                client.exitGame();
                client.setIsPlayerTurn(false);
                client.setHumanPlayer();
                Thread.sleep(1000);

                //                        if (aiClient.getGameStarted()) {
                //                            System.out.println("Goes into here. 1");
                //                            if (aiClient.getFirstPlayer()) {
                //                                System.out.println("Goes into here. 2");
                //                                int movetoSendToServer = aiClient.getAIPlayer().getNextMove().getMoveIndexForServer();
                //                                aiClient.sendMove(movetoSendToServer);
                //                            }
                //                        }


            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Receives a chat message from the server and prints it to the console.
     *
     * @param senderUsername The username of the message sender.
     * @param message        The received chat message.
     */


    /**
     * Handles the event when the client disconnects from the server.
     * Prints a message to the console, handles any necessary cleanup,
     * and exits the application.
     */
    @Override
    public void handleDisconnect() {
        System.out.println("Connection lost. Exiting...");
        System.exit(0);
    }


}

