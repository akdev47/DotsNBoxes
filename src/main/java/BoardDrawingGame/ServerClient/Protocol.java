package BoardDrawingGame.ServerClient;

/**
 * Protocol class with constants for creating BoardDrawingGame.ServerClient.protocol messages
 */
public final class Protocol {

    //client protocol
    public static final String HELLO = "HELLO";
    public static final String LOGIN = "LOGIN";
    public static final String LIST = "LIST";
    public static final String QUEUE = "QUEUE";

    public static final String MOVE = "MOVE";

    public static final String SEPARATOR = "~";


    //server protocol

    public static final String ALREADYLOGGEDIN= "ALREADYLOGGEDIN";
    public static final String NEWGAME = "NEWGAME";

    public static final String GAMEOVER = "GAMEOVER";

    /**
     * Private constructor to prevent instantiation of the Protocol class.
     * This constructor ensures that the Protocol class cannot be instantiated.
     */
    private Protocol() {
        // Private constructor to prevent instantiation
    }
}
