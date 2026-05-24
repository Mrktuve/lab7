package common.commands;

public class FilterStartsWithName implements Command {
    private final String prefix;

    public FilterStartsWithName(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }
}