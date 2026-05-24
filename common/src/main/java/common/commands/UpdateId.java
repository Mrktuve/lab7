package common.commands;

import common.model.Worker;

public class UpdateId implements Command {
    private final Long id;
    private final Worker worker;

    public UpdateId(Long id, Worker worker) {
        this.id = id;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }

    @Override
    public String getName() {
        return "update";
    }
}