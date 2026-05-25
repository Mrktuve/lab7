package client.auth;

public class ClientAuthManager {

    private String login;
    private String password;

    public void setCredentials(String login, String password) {

        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthorized() {
        return login != null && password != null;
    }
}