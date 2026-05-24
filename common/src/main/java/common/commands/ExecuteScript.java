package common.commands;

public class ExecuteScript implements Command {
    private final String fileName;

    public ExecuteScript(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getName() {
        return "execute_script";
    }
}