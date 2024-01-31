

package BoardDrawingGame.ServerClient.Client;





import BoardDrawingGame.ServerClient.Protocol;
import java.io.IOException;
import java.util.Scanner;

public class AIClientTUI implements ClientListener{



    public static void main(String[] args) {
        AIClientTUI aiClientTUI = new AIClientTUI();
        aiClientTUI.runTUI();
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


            Client aiClient = new Client(serverAddress, serverPort);
            aiClient.setAIPlayer();
            aiClient.addListener(this);

            String username = "";

            while (!aiClient.getLogInSuccesfull()) {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                aiClient.sendUsername(username);
                Thread.sleep(500);
            }
            aiClient.setUsername(username);
            System.out.println("Username: " + aiClient.getUsername());
            aiClient.sendHello(username);




            while (true) {
                Thread.sleep(500);
                while (!aiClient.getGameStarted() || !aiClient.getInQueue()) {
                    //                    System.out.println("Is player in queue: " + playerInQueue);
                    Thread.sleep(1000);

                    String message = "";
                    if (!aiClient.getInQueue()) {
                        System.out.print("Enter a command message (or type 'quit' to exit): ");
                        message = scanner.nextLine();
                    }

                    if ("quit".equalsIgnoreCase(message)) {
                        aiClient.close();
                        break;
                    }

                    if (message.equalsIgnoreCase(Protocol.LIST)) {
                        aiClient.sendList();
                    } else if (message.equalsIgnoreCase(Protocol.QUEUE)) {
                        aiClient.setInQueue(true);
                        aiClient.sendQueue();
                    }
                }






                 while (!aiClient.getGameover()) {
                     Thread.sleep(500);
                     while (!aiClient.getPlayerTurn()) {
                         Thread.sleep(1000);
                         if(aiClient.getGameover()) {
                             break;
                         }
                     }


                     if (aiClient.getPlayerTurn()) {

                         if (aiClient.getAIPlayer().getNextMove() != null) {
                             int moveToSendToServer = aiClient.getAIPlayer().getNextMove()
                                     .getMoveIndexForServer();
                             aiClient.sendMove(moveToSendToServer);
                         }
                     }

                 }
                System.out.println("Game Over");
                aiClient.setInQueue(false);
                aiClient.setGameStarted(false);
                aiClient.exitGame();
                aiClient.setIsPlayerTurn(false);
                aiClient.setAIPlayer();
                Thread.sleep(1000);


            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




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

