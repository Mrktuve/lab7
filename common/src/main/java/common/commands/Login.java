package common.commands;

import java.io.Serializable;

public class Login implements Command,
        Serializable {

    private static final long
            serialVersionUID = 1L;

    @Override
    public String getName() {
        return "login";
    }

    public String getDescription() {
        return "авторизация пользователя";
    }
}
