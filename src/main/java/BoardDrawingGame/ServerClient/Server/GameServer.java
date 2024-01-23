package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.ServerClient.SocketServer;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class GameServer extends SocketServer {

    private List<ClientHandler> clients;


    /**
     * Constructs a new ChatServer
     * @param port the port to listen on
     * @throws IOException if the server socket cannot be created, for example, because the port is already bound.
     */
    public GameServer(int port) throws IOException {
        super(port);
        clients = new ArrayList<>();
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
     * Adds the received client to the client list. Client enters the chat.
     *
     * @param client The client that will be added to the client list.
     */
    public synchronized  void addClient(ClientHandler client) {
        System.out.println("Recieved username: " + client.getUserName());
        clients.add(client);
    }


    /**
     * Removes the received client to the client list. Client exits the chat.
     *
     * @param client The client that will be removed to the client list.
     */
    public synchronized  void removeClient(ClientHandler client) {
        System.out.println(client.getUserName()+ " disconnected");
        clients.remove(client);
    }

    /**
     * Receives a chat message from the client and send it to all connected clients.
     *
     * @param client The client whose message will be sent to other clients.
     */
    public void handleChatMessage(ClientHandler client, String message) {
        System.out.println("Recieved chat message from: " + client.getUserName() + ": " + message);
        for(ClientHandler c : clients) {
            c.sendChatMessage(client.getUserName(), message);
        }
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
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

}
