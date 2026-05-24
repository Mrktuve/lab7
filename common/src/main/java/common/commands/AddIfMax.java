package common.commands;

import common.model.Worker;

public class AddIfMax implements Command {
    private final Worker worker;

    public AddIfMax(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    @Override
    public String getName() {
        return "add_if_max";
    }
}