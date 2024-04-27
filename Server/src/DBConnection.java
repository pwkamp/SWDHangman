import com.mysql.cj.Query;

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

    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, coins INT DEFAULT 30, game_earnings INT DEFAULT 0);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createGameTable() {
        String query = "CREATE TABLE IF NOT EXISTS hangman_games (game_id INT AUTO_INCREMENT PRIMARY KEY, join_code VARCHAR(6) UNIQUE NOT NULL, game_leader VARCHAR(255) NOT NULL, game_state VARCHAR(255) DEFAULT 'waiting', word VARCHAR(255), letters_guessed VARCHAR(255), player1 VARCHAR(255), player2 VARCHAR(255), player3 VARCHAR(255), player4 VARCHAR(255), player5 VARCHAR(255), player6 VARCHAR(255), player7 VARCHAR(255), player8 VARCHAR(255), player9 VARCHAR(255))";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String user) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users WHERE username = '" + user + "';");
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Returns false if user not created or true if user created
    public boolean createUser(String user, String pass) {
        user = user.toLowerCase();
        if (userExists(user)) {
            return false;
        }
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO users (username, password) VALUES ('" + user + "', '" + pass + "');");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createUser(User user) {
        return createUser(user.getUsername(), user.getPassword());
    }

    public User getUser(String user) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM users WHERE username = '" + user + "';");
            if (set.next()) {
                return new User(set.getString("username"), set.getString("password"), set.getInt("coins"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User("", "", -1);
    }

    // Returns false if coins not set or true if coins set
    public boolean setUserCoins(String user, int coins) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE users SET coins = " + coins + " WHERE username = '" + user + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setUserCoins(User user) {
        return setUserCoins(user.getUsername(), user.getCoins());
    }

    public boolean addUserCoins(String user, int coins) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            int userCoins = getUserCoins(user);
            if (userCoins == -1) {
                return false;
            } else {
                return setUserCoins(user, userCoins + coins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserCoins(User user, int coins) {
        return addUserCoins(user.getUsername(), coins);
    }

    public int getUserCoins(String user) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT coins FROM users WHERE username = '" + user + "';");
            if (set.next()) {
                return set.getInt("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Returns false if coins not set or true if coins set
    public boolean setUserEarnings(String user, int coins) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE users SET game_earnings = " + coins + " WHERE username = '" + user + "';");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setUserEarnings(User user) {
        return setUserEarnings(user.getUsername(), user.getCoins());
    }

    public boolean addUserEarnings(String user, int coins) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            int userEarnings = getUserEarnings(user);
            if (userEarnings == -1) {
                return false;
            } else {
                return setUserEarnings(user, userEarnings + coins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserEarnings(User user, int coins) {
        return addUserCoins(user.getUsername(), coins);
    }

    public int getUserEarnings(String user) {
        user = user.toLowerCase();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT game_earnings FROM users WHERE username = '" + user + "';");
            if (set.next()) {
                return set.getInt("game_earnings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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

}
