public class User {
    private String username;
    private String password;
    private int coins;

    private int gameEarnings;

    public User(String username, String password, int coins) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        gameEarnings = 0;
    }

    public User(String username, String password, int coins, int gameEarnings) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.gameEarnings = gameEarnings;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getGameEarnings() {
        return gameEarnings;
    }

    public void setGameEarnings(int gameEarnings) {
        this.gameEarnings = gameEarnings;
    }
}
