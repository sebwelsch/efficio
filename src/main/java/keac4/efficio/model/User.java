package keac4.efficio.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String confirmPassword;


    public User() {}


    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}