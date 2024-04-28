import java.net.Socket;
import java.util.ArrayList;

// This class is responsible for handling the game logic
public class GameHandler implements Runnable{
    ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    GameHandler() {
        // import words list
        Words words = new Words("words.txt");

    }

    @Override
    public void run() {

    }

    //TODO
    public boolean addClient(ClientHandler client) {
        if (clients.size() > 4) {
            return false;
        }
        clients.add(client);
        return true;
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void startGame() {
        //TODO
    }
}
