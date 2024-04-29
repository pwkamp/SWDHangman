import java.util.ArrayList;
import java.util.Arrays;

// This class is responsible for handling the game logic
public class GameHandler implements Runnable{
    ArrayList<ClientHandler> clients = new ArrayList<>();
    ClientHandler leader;
    DBConnection dbConnection;
    String joinCode;

    private String state = "waiting";
    String word = "";
    String currentlyRevealedWord = "";
    private String[] wordOptions;

    public GameHandler(ClientHandler leader, DBConnection dbConnection) {
        this.leader = leader;
        clients.add(leader);
        this.dbConnection = dbConnection;
        joinCode = dbConnection.generateGameCode();

        dbConnection.createGame(leader.getUser().getUsername(), joinCode);
        dbConnection.joinGame(leader.getUser().getUsername(), joinCode);
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
        state = "waiting";
        dbConnection.log(joinCode + ": Awaiting players");

        while (true) {
            String[] message = leader.awaitMessage().split(" ");
            if (message[0].equals("START")) {
                dbConnection.setGameState(joinCode, "active");
                state = "active";
                dbConnection.log(joinCode + ": leader started game");
                System.out.println(joinCode + ": leader started game");

                messageClients("START " + word.length());
                return true;
            } else if (message[0].equals("ENDGAME")) {
                dbConnection.setGameState(joinCode, "inactive");
                state = "inactive";
                dbConnection.log(joinCode + ": leader ended game");
                System.out.println(joinCode + ": leader ended game");
                messageClients("ENDGAME");
                return false;
            } else if (message[0].equals("WORD")) {
                word = wordOptions[Integer.parseInt(message[1]) - 1];

                StringBuilder wordLengthPlaceholder = new StringBuilder();
                wordLengthPlaceholder.append("_".repeat(Math.max(0, word.length())));
                currentlyRevealedWord = wordLengthPlaceholder.toString();
                dbConnection.setGuessedData(joinCode, currentlyRevealedWord);

                dbConnection.log(joinCode + ": leader set word to " + word);
                System.out.println(joinCode + ": leader set word to " + word);
                dbConnection.setWord(joinCode, word);
                messageClients("WORD success " + word.length());
            } else {
                dbConnection.log(joinCode + ": Invalid message " + message);
                System.out.println(joinCode + ": Invalid message " + message);
                return false;
            }

            for (ClientHandler client : clients) {
                String[] message2 = client.awaitMessage().split(" ");
                if (message2[0].equals("LEAVEGAME")) {
                    clients.remove(client);
                    dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " left the game");
                    dbConnection.leaveGame(client.getUser().getUsername(), joinCode);
                }
            }

        }
    }

    private void gameLoop() {
        dbConnection.log(joinCode + ": Game loop entered");
        while (true) {
            for (ClientHandler client : clients) {
                if (client.equals(leader) || client.hasLostRound()) {
                    continue;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                client.sendMessage("YOURTURN");
                String[] message = client.awaitMessage().split(" ");
                if (message[0].equals("LETTER")) {
                    dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " guessed " + message[1]);
                    System.out.println(joinCode + ": " + client.getUser().getUsername() + " guessed " + message[1]);

                    if (word.contains(message[1])) {
                        dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " guessed correctly");
                        System.out.println(joinCode + ": " + client.getUser().getUsername() + " guessed correctly");

                        for (int i = 0; i < word.length(); i++) {
                            if (word.charAt(i) == message[1].charAt(0)) {
                                currentlyRevealedWord = currentlyRevealedWord.substring(0, i) + message[1] + currentlyRevealedWord.substring(i + 1);
                            }
                        }

                        System.out.println(Arrays.toString(currentlyRevealedWord.split("_ ")));
                        if (Arrays.toString(currentlyRevealedWord.split("_ ")).equals(word)) {
                            dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " won");
                            System.out.println(joinCode + ": " + client.getUser().getUsername() + " won");
                            messageClients("WINNER " + client.getUser().getUsername());
                        } else {
                            messageClients("CORRECTLETTER " + message[1] + " " + currentlyRevealedWord);
                        }
                    } else {
                        dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " guessed incorrectly");
                        System.out.println(joinCode + ": " + client.getUser().getUsername() + " guessed incorrectly");
                        messageClients("INCORRECTLETTER " + message[1]);
                    }
                } else if (message[0].equals("WORD")) {
                    dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " guessed word " + message[1]);
                    System.out.println(joinCode + ": " + client.getUser().getUsername() + " guessed word " + message[1]);

                    if (word.equals(message[1])) {
                        dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " won");
                        System.out.println(joinCode + ": " + client.getUser().getUsername() + " won");
                        messageClients("WINNER " + client.getUser().getUsername());
                    } else {
                        dbConnection.log(joinCode + ": " + client.getUser().getUsername() + " guessed word incorrectly");
                        System.out.println(joinCode + ": " + client.getUser().getUsername() + " guessed word incorrectly");
                        client.setLostRound(true);
                        messageClients("INCORRECTWORD " + client.getUser().getUsername());
                    }
                } else {
                    dbConnection.log(joinCode + ": Invalid message " + message);
                    System.out.println(joinCode + ": Invalid message " + message);
                    return;
                }
            }
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        dbConnection.joinGame(client.getUser().getUsername(), joinCode);
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

    public void setState(String state) {
        this.state = state;
    }

     /////////////////// GETTERS ///////////////////

    public String getJoinCode(){
        return joinCode;
    }

    public String[] getWordOptions() {
        return wordOptions;
    }

    public String getState() {
        return state;
    }
}
