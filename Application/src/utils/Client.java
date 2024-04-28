package utils;

import javafx.scene.text.Text;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The utils.Client class represents the client in the networked application.
 * I.E the client and server can run on different machines and communicate over the network.
 * It handles the creation of the client socket, input and output streams, and the processing of the connection.
 * @author Peter Kamp
 */
public final class Client implements Runnable {

    private final static Client INSTANCE = new Client("localhost", 8080);
    private final String chatServer;
    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String message = "";

    private final int port;

    private String gameCode;
    private String username;
    private String password;
    private String coins;
    private String[] wordOptions;


    /**
     * Constructs a new client with the specified host and port.
     * @param host the IP address of the server
     * @param port the port of the server
     * @author Peter Kamp
     */
    public Client(String host, int port) {
        chatServer = host;
        this.port = port;
    }

    public static Client getInstance() {
        return INSTANCE;
    }

    /**
     * Starts the client by attempting to connect to the server, creating I/O streams, and processing the connection.
     * Then, maintains the connection indefinitely allowing the user to continue encoding and decoding messages.
     * When the server sends the message "SERVER ► TERMINATE", the connection is closed.
     * @author Peter Kamp
     */
    public void run() {
        try {
            displayMessage("Attempting to Connect");
            client = new Socket(InetAddress.getByName(chatServer), port);
            displayMessage("\nConnected to: " + client.getInetAddress().getHostName());

            outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.flush();

            inputStream = new ObjectInputStream(client.getInputStream());
            displayMessage("\nI/O streams created");

            processConnection();
        } catch (EOFException eofException) {
            displayMessage("\nConnection Terminated");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Processes the connection by reading messages from the input stream until the server sends a termination message.
     * @throws IOException if an I/O error occurs when reading from the input stream
     * @author Peter Kamp
     */
    private void processConnection() throws IOException {

        do {
            try {
                message = (String) inputStream.readObject();
                System.out.println(message);
                displayMessage(message);
            } catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object.");
            }
        } while (!message.equals("\nSERVER ► TERMINATE"));
    }

    /**
     * Closes the connection and the I/O streams.
     * @author Peter Kamp
     */
    private void closeConnection() {
        displayMessage("\nClosing connection");

        try {
            outputStream.close();
            inputStream.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Sends data to the server by writing a String to the output stream.
     * @param message the string to send
     * @author Peter Kamp
     */
    public void sendData(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush(); // flush data to output
            displayMessage("\n" + message);
        } catch (IOException ioException) {
            displayMessage("\nError writing object");
        }
    }

    /**
     * Displays a message
     * @param message the message to display
     * @author Peter Kamp
     */
    private void displayMessage(final String message) {
        System.out.println(message);
    }

    public String getMessage() {
        Debugger.debug("Message from Server: " + message);
        return message;
    }

    public void awaitMessage() {
        message = "";
        while (message.equals("")) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void sendInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("INFO ");
        sb.append(username);
        sb.append(" ");
        sb.append(coins);
        sb.append(" ");
        sb.append(gameCode);
        sendData(sb.toString());
    }

    public void setUsername(String text) {
        this.username = text;
    }

    public String getUsername() {
        return username;
    }

    public void setWordOptions(String[] wordOptions) {
        this.wordOptions = wordOptions;
    }

    public String[] getWordOptions() {
        return wordOptions;
    }
}