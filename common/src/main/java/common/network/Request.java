package common.network;

import common.commands.Command;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Command command;
    private final String login;
    private final String password;

    public Request(Command command,
                   String login,
                   String password) {

        this.command = command;
        this.login = login;
        this.password = password;
    }

    public Command getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}