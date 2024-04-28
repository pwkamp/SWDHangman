import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

// Receives client socket and handles communication with client
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final DBConnection dbConnection;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final Server parentServer;
    private GameHandler gameHandler;
    private boolean isConnectionOpen;
    private User user;
    String state;

    private String message = "";

    private String username;


    public boolean isConnectionOpen() {
        return isConnectionOpen;
    }

    public ClientHandler(Socket socket, DBConnection dbConnection, Server parentServer) {
        dbConnection.log("Client connected: " + socket.getInetAddress() + ":" + socket.getPort());

        this.parentServer = parentServer;
        this.socket = socket;
        this.dbConnection = dbConnection;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            dbConnection.log("Error creating streams for " + socket.getInetAddress() + ": " + e.getMessage(), Arrays.toString(e.getStackTrace()));
            System.out.println("Error creating streams: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        loginClient();

        joinGame();

        awaitGameHandler();

        runGame();
    }

    private void runGame() {
    }

    /**
     * Closes the connection and the I/O streams.
     * @author Peter Kamp
     */
    private void closeConnection() {
        //displayMessage("\nClosing connection");

        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void loginClient() {
        while (true) {
            String messageText = receiveMessage();
            String[] message = new String[0];
            if (messageText != null) {
                message = messageText.split(" ");
            }

            // invalid message length
            if (message.length != 3) {
                sendMessage("Invalid request");
                continue;
            }

            // create user
            if (message[0].equals("CREATE")) {
                boolean isSuccessful = dbConnection.createUser(message[1], message[2]);
                if (isSuccessful) {
                    dbConnection.log("User created: " + message[1]);
                    sendMessage("success");
                    return;
                }

                sendMessage("User already exists");
                continue;
            }

            // login
            if (message[0].equals("LOGIN")) {
                if (!dbConnection.validateUser(message[1], message[2])) {
                    dbConnection.log("Failed login attempt: " + message[1]);
                    sendMessage("Username or password incorrect");
                    continue;
                } else {
                    dbConnection.log("User logged in: " + message[1]);
                    sendMessage("success");
                    return;
                }
            }

            // Valid message results in early return. Reaching this point means something went wrong
            sendMessage("Invalid request");
        }
    }

    private void joinGame() {
        while (true) {
            String messageText = receiveMessage();
            String[] message = new String[0];
            if (messageText != null) {
                message = messageText.split(" ");
            }

            // join game
            if (message[0].equals("JOIN")) {
                if (message.length != 3) {
                    sendMessage("Invalid request");
                    continue;
                }

                if (!parentServer.gameExistsActive(message[1])) {
                    sendMessage("Game does not exist");
                    continue;
                }
                
                setUsername(message[2]);
                parentServer.joinGame(this, message[1], message[2]);
                return;
            }

            // create game
            if (message[0].equals("CREATEGAME")) {
                if (message.length != 1) {
                    sendMessage("Invalid request");
                    continue;
                }

                if (parentServer.createGame(this)) {
                    return;
                }
                continue;
            }


            // Valid message results in early return. Reaching this point means something went wrong
            sendMessage("Invalid request");
        }
    }

    public String receiveMessage() {
        message = "";
        try {
            message = (String) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Thread.currentThread().interrupt();
        }
        return message;
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void awaitGameHandler() {
        while (true) {
            if (this.gameHandler != null) {
                return;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public Socket getSocket() {
        return socket;
    }

    public void awaitMessage() {
        message = "";
        while (message.equals("")) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public String getMessage() {
        return message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
