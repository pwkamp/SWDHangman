public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String serialize() {
        return username + " " + password;
    }

    public static User deserialize(String serialized) {
        String[] parts = serialized.split(" ");
        return new User(parts[0], parts[1]);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
