package BoardDrawingGame.ServerClient.Server;


import BoardDrawingGame.ServerClient.SocketConnection;
import BoardDrawingGame.ServerClient.protocol.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ServerConnection extends SocketConnection {

    ClientHandler clientHandler;
    private BufferedReader reader;


    protected ServerConnection(Socket socket,GameServer chatServer) throws IOException {
        super(socket);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientHandler = new ClientHandler(this,chatServer);
        setClientHandler(clientHandler);
    }


    @Override
    protected void handleMessage(String message) {
        //        System.out.println(message);

        try {
            if(message.contains(Protocol.SEPARATOR)) {
                String[] splitMessage = message.split(Protocol.SEPARATOR);

                if (splitMessage.length == 2) {
                    String firstCommand = splitMessage[0];

                    if (firstCommand.equals(Protocol.LOGIN)) {
                        getClientHandler().recieveUsername(splitMessage[1]);
                    }
                }

//                else if(firstCommand.equals(Protocol.SAY) && clientHandler.getUserNameSet()) {
//                    getClientHandler().recieveChatMessage(splitMessage[1]);
//                }

            }
            else {
                if(message.equals(Protocol.LIST)) {
                    getClientHandler().recieveListCommand();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleDisconnect() {
        getClientHandler().handleDisconnect();
    }

    public void run() {
        try {
            while (true) {
                String message = this.reader.readLine();
                if (message == null) {
                    break;
                }
                handleMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handleDisconnect();
        }
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void sendChatMessage(String username,String message) {
//        super.sendMessage(Protocol.FROM + Protocol.SEPARATOR + username + Protocol.SEPARATOR+message);
        //        super.(Protocol.FROM + Protocol.SEPARATOR + clientHandler.getUserName() + Protocol.SEPARATOR+message);
    }

    public void sendLogIn() {
        System.out.println("LOGIN");
        super.sendMessage(Protocol.LOGIN);
    }

    public void sendAlreadyLoggedIn() {
        System.out.println("ALREADY LOGGED IN");
        super.sendMessage(Protocol.ALREADYLOGGEDIN);
    }

    public void sendList(String formattedList) {
        System.out.println("LIST");
        super.sendMessage(formattedList);
    }
}
