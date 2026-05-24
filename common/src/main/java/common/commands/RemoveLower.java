package common.commands;

import common.model.Worker;

public class RemoveLower implements Command {
    private final Worker worker;

    public RemoveLower(Worker worker) {
        this.worker = worker;
    }

    public Worker getWorker() {
        return worker;
    }

    @Override
    public String getName() {
        return "remove_lower";
    }
}