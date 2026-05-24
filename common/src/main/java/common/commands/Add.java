package common.commands;

import common.model.Worker;

public class Add implements Command {
    private final Worker worker;

    public Add(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    @Override
    public String getName() {
        return "add";
    }
}