import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

// This class is responsible for handling the game logic
public class GameHandler implements Runnable{
    ArrayList<ClientHandler> clients = new ArrayList<>();

    ClientHandler leader;

    DBConnection dbConnection;

    String joinCode;

    public GameHandler(ClientHandler leader, DBConnection dbConnection){
        this.leader = leader;
        this.dbConnection = dbConnection;
        joinCode = dbConnection.generateGameCode();
        //TODO: Implement get name function
        dbConnection.createGame("test", joinCode);
        dbConnection.log("Game created with join code: " + joinCode);
    }

    @Override
    public void run() {
        if (awaitPlayers()) {
            gameLoop();
        }
    }

    private boolean awaitPlayers() {
        leader.awaitMessage();
        String message = leader.getMessage();
        if (message.equals("START")) {
            return true;
        }
        return false;
    }

    private void gameLoop() {
        leader.sendMessage("START");
        for (ClientHandler client : clients) {
            client.sendMessage("START");
        }
        System.out.println("Game started: " + joinCode);
        while (true) {

        }
    }

    public String getJoinCode(){
        return joinCode;
    }


    public void addClient(ClientHandler client) {
        clients.add(client);
        updatePlayers();
    }

    private void updatePlayers() {
        //dbConnection.get
    }
}
