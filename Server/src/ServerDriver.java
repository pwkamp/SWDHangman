import java.sql.ResultSet;

public class ServerDriver {

    //TODO: Implement Server Functionality
    public static void main(String[] args) {

        // Example on how to use Words class
        Words words = new Words("res/SWD_words.csv");

        System.out.println("Random word: " + words.getRandomWord());

        DBConnection dbConnection = new DBConnection(DBConnection.TEST_DATABASE_URL, "root", "");

        // Example on how to use DBConnection class
        //If errors, see: https://www.javatpoint.com/no-suitable-driver-found-for-jdbc

        ResultSet set;
        try {
            dbConnection.connect();
            dbConnection.createUsersTable();
            dbConnection.createGameTable();

            System.out.println("User created: " + dbConnection.createUser("test", "password"));

            if (dbConnection.userExists("test")) {
                System.out.println("User exists");
                User user = dbConnection.getUser("test");
                System.out.println("Username: " + user.getUsername());
                System.out.println("Password: " + user.getPassword());
                System.out.println("Coins: " + user.getCoins());

                dbConnection.addUserCoins("test", 100);
                user = dbConnection.getUser("test");
                System.out.println("New Coins: " + user.getCoins());


            } else {
                System.out.println("User does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
