import java.util.ArrayList;
import java.util.Arrays;

// This class is responsible for handling the game logic
public class GameHandler implements Runnable{
    ArrayList<ClientHandler> clients = new ArrayList<>();

    ClientHandler leader;

    DBConnection dbConnection;

    String joinCode;

    String word = "";

    String currentlyRevealedWord = "";
    private String[] wordOptions;

    public GameHandler(ClientHandler leader, DBConnection dbConnection) {
        this.leader = leader;
        clients.add(leader);
        this.dbConnection = dbConnection;
        joinCode = dbConnection.generateGameCode();
        //TODO: Implement get name function
        dbConnection.createGame("test", joinCode);
        dbConnection.log("Game created with join code: " + joinCode);
    }

    @Override
    public void run() {
        dbConnection.log(joinCode + ": Game started");
        if(awaitPlayers()) {
            gameLoop();
        }
    }

    private boolean awaitPlayers() {
        dbConnection.log(joinCode + ": Awaiting players");

        while (true) {
            String[] message = leader.awaitMessage().split(" ");
            if (message[0].equals("START")) {
                dbConnection.log(joinCode + ": leader started game");
                System.out.println(joinCode + ": leader started game");

                messageClients("START " + word.length());
                return true;
            } else if (message[0].equals("ENDGAME")) {
                dbConnection.log(joinCode + ": leader ended game");
                System.out.println(joinCode + ": leader ended game");
                messageClients("ENDGAME");
                return false;
            } else if (message[0].equals("WORD")) {
                word = wordOptions[Integer.parseInt(message[1]) - 1];

                StringBuilder wordLengthPlaceholder = new StringBuilder();
                wordLengthPlaceholder.append("_".repeat(Math.max(0, word.length())));
                currentlyRevealedWord = wordLengthPlaceholder.toString();

                dbConnection.log(joinCode + ": leader set word to " + word);
                System.out.println(joinCode + ": leader set word to " + word);
                messageClients("WORD success " + word.length());
            } else {
                dbConnection.log(joinCode + ": Invalid message " + message);
                System.out.println(joinCode + ": Invalid message " + message);
                return false;
            }
        }
    }

    private void gameLoop() {
        dbConnection.log(joinCode + ": Game loop entered");
        while (true) {
            for (ClientHandler client : clients) {
                if (client.equals(leader)) {
                    continue;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                client.sendMessage("YOURTURN");
                client.awaitMessage();
                String message = client.getMessage();
                String[] messageArray = message.split(" ");
                if (messageArray[0].equals("LETTER")) {
                    dbConnection.log(joinCode + ": " + client.getUsername() + " guessed " + messageArray[1]);
                    System.out.println(joinCode + ": " + client.getUsername() + " guessed " + messageArray[1]);
                }
                if (word.contains(messageArray[1])) {
                    dbConnection.log(joinCode + ": " + client.getUsername() + " guessed correctly");
                    System.out.println(joinCode + ": " + client.getUsername() + " guessed correctly");

                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == messageArray[1].charAt(0)) {
                            currentlyRevealedWord = currentlyRevealedWord.substring(0, i) + messageArray[1] + currentlyRevealedWord.substring(i + 1);
                        }
                    }

                    System.out.println(Arrays.toString(currentlyRevealedWord.split("_ ")));
                    if (Arrays.toString(currentlyRevealedWord.split("_ ")).equals(word)) {
                        dbConnection.log(joinCode + ": " + client.getUsername() + " won");
                        System.out.println(joinCode + ": " + client.getUsername() + " won");
                        messageClients("WINNER " + client.getUsername());
                    } else {
                        messageClients("CORRECTLETTER " + messageArray[1] + " " + currentlyRevealedWord);
                    }
                } else {
                    dbConnection.log(joinCode + ": " + client.getUsername() + " guessed incorrectly");
                    System.out.println(joinCode + ": " + client.getUsername() + " guessed incorrectly");
                    messageClients("INCORRECTLETTER " + messageArray[1]);
                }
            }
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        updatePlayers();
    }

    private void updatePlayers() {
        //dbConnection.get
    }

    private void messageClients(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void createGame() {
        Words words = new Words("res/SWD_words.csv");
        String[] wordOptions = new String[]{words.getRandomWord(), words.getRandomWord(), words.getRandomWord()};
        setWordOptions(wordOptions);
    }

    /////////////////// SETTERS ///////////////////

    public void setWordOptions(String[] wordOptions) {
        this.wordOptions = wordOptions;
    }

     /////////////////// GETTERS ///////////////////

    public String getJoinCode(){
        return joinCode;
    }

    public String[] getWordOptions() {
        return wordOptions;
    }
}
