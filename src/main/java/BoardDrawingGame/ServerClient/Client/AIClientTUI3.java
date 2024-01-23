package BoardDrawingGame.ServerClient.Client;





import BoardDrawingGame.ServerClient.protocol.Protocol;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AIClientTUI3 implements ClientListener{



    public static void main(String[] args) {
        AIClientTUI3 AIClientTUI3 = new AIClientTUI3();
        AIClientTUI3.runTUI();
    }

    /**
     * Runs the Text User Interface for the chat client.
     * It prompts the user for server address, port, and username, and allows the user
     * to send chat messages until they decide to quit.
     */
    public void runTUI() {
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.print("Enter server address: ");
            String serverAddress = scanner.nextLine();
            System.out.print("Enter server port: ");
            int serverPort = scanner.nextInt();
            scanner.nextLine();



            AIClient AIClient = new AIClient(serverAddress, serverPort);
            AIClient.addListener(this);

            String username = "";

            while (!AIClient.getLogInSuccesfull()) {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                AIClient.sendUsername(username);
                Thread.sleep(5);
            }
            AIClient.setUsername(username);
            System.out.println("Username: " + AIClient.getUsername());






            while (true) {
                System.out.print("Enter a message (or type 'quit' to exit): ");
                String message = scanner.nextLine();

                if ("quit".equalsIgnoreCase(message)) {
                    AIClient.close();
                    break;
                }

                if(message.equalsIgnoreCase(Protocol.LIST)) {
                    AIClient.sendList();
                }



            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }

    /**
     * Receives a chat message from the server and prints it to the console.
     *
     * @param senderUsername The username of the message sender.
     * @param message        The received chat message.
     */
    @Override
    public void receiveChatMessage(String senderUsername, String message) {
        System.out.println(senderUsername + ": " + message);
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

