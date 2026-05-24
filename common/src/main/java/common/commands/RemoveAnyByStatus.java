package common.commands;

import common.enums.Status;

public class RemoveAnyByStatus implements Command {
    private final Status status;

    public RemoveAnyByStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return "remove_any_by_status";
    }
}