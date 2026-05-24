package common.commands;

public class RemoveById implements Command {
    private final Long id;

    public RemoveById(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}