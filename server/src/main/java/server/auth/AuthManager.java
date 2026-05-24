package server.auth;

import server.database.UserDAO;

public class AuthManager {

    private final UserDAO userDAO;

    public AuthManager(
            UserDAO userDAO
    ) {

        this.userDAO = userDAO;
    }

    public boolean register(
            String login,
            String password
    ) {

        return userDAO.register(
                login,
                password
        );
    }

    public boolean login(
            String login,
            String password
    ) {

        return userDAO.login(
                login,
                password
        );
    }

    public boolean authenticate(
            String login,
            String password
    ) {

        return login(
                login,
                password
        );
    }
}