package utils;

import javafx.scene.text.Text;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

// 23535

/**
 * The utils.Client class represents the client in the networked application.
 * I.E the client and server can run on different machines and communicate over the network.
 * It handles the creation of the client socket, input and output streams, and the processing of the connection.
 * @author Peter Kamp
 */
public final class Client implements Runnable {

    private static final Client INSTANCE = new Client();
    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private static String gameCode;
    private static String username;
    private static String password;
    private static String coins;
    private static String[] wordOptions;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * Constructs a new client with the specified host and port.
     * @author Peter Kamp
     */
    public Client() {
    }

    public static Client getInstance() {
        return INSTANCE;
    }

    public void connect(String ip, int port) throws IOException {
        displayMessage("Attempting to Connect");
        client = new Socket(InetAddress.getByName(ip), port);
        displayMessage("\nConnected to: " + client.getInetAddress().getHostName());

        outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(client.getInputStream());
        displayMessage("\nI/O streams created");
    }

    /**
     * Starts the client by attempting to connect to the server, creating I/O streams, and processing the connection.
     * Then, maintains the connection indefinitely allowing the user to continue encoding and decoding messages.
     * When the server sends the message "SERVER ► TERMINATE", the connection is closed.
     * @author Peter Kamp
     */
    public void run() {
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

    public String awaitMessage() {
        String message = null;
        try {
            message = (String) inputStream.readObject();
        } catch (ClassNotFoundException classNotFoundException) {
            displayMessage("\nUnknown object type received");
        } catch (IOException ioException) {
            displayMessage("\nError reading object");
        }

        return message;
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