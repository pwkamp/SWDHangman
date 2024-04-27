public class User {
    private String username;
    private String password;
    private int coins;

    public User(String username, String password, int coins) {
        this.username = username;
        this.password = password;
        this.coins = coins;
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
}
