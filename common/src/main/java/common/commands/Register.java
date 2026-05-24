package common.commands;

import java.io.Serializable;

public class Register implements Command,
        Serializable {

    private static final long
            serialVersionUID = 1L;

    @Override
    public String getName() {
        return "register";
    }

    public String getDescription() {
        return "регистрация пользователя";
    }
}