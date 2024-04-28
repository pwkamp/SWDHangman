import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// waits for incoming connections and passes off responsibility to a new thread
public class Server {
    private ServerSocket server;
    private DBConnection dbConnection;
    ExecutorService executorService;

    private ArrayList<GameHandler> games = new ArrayList<>();

    public Server(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        executorService = Executors.newCachedThreadPool();
    }

    public void startServer(int port) {
        dbConnection.log("Attempting to start server on port " + port);
        try {
            server = new ServerSocket(port);
            dbConnection.log("Server started on port " + port);
        } catch (IOException e) {
            dbConnection.log("Error starting server: " + e.getMessage());
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                ClientHandler clientHandler = new ClientHandler(server.accept(), dbConnection, this);

                executorService.execute(clientHandler);

            } catch (IOException e) {
                dbConnection.log("Error accepting client: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    // TODO x3
    public boolean createGame(ClientHandler client){
        // create a new game
        GameHandler gameHandler = new GameHandler(client, dbConnection);
        client.setGameHandler(gameHandler);
        client.sendMessage("CREATEGAME " + "success " + gameHandler.getJoinCode());
        games.add(gameHandler);
        return true;
    }
    public void joinGame(ClientHandler client, String code){
        // join an existing game
        for (GameHandler game : games) {
            if (game.getJoinCode().equals(code)) {
                game.addClient(client);
                client.setGameHandler(game);
                client.sendMessage("JOIN success");
                return;
            }
        }
        client.sendMessage("JOIN fail");
    }
    public boolean gameExistsActive(String code){
        for (GameHandler game : games) {
            if (game.getJoinCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
