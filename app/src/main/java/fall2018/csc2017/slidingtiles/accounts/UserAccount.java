package accounts;

public class UserAccount extends Account {
    private String username;
    private String password;

    public UserAccount(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}
