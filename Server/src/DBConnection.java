import java.sql.*;

//TODO: Add more database connection functionality
//If errors, see: https://www.javatpoint.com/no-suitable-driver-found-for-jdbc
public class DBConnection {
    public static String TEST_DATABASE_URL = "jdbc:mysql://localhost/team1";

    private Connection connection;

    private String username;
    private String password;

    private String url;

    public DBConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet sendQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, coins INT NOT NULL DEFAULT 0);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String user) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT COUNT(*) FROM users WHERE username = '" + user + "';");
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createUser(String user, String pass) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO users (username, password) VALUES ('" + user + "', '" + pass + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
