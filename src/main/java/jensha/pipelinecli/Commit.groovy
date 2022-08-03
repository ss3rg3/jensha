package jensha.pipelinecli

class Commit {

    private List<Command> commands = new ArrayList<>()

    boolean hasCommand(String commandName) {
        for (Command command : commands) {
            if (commandName == command.name) {
                return true
            }
        }
        return false
    }

    Command getCommand(String commandName) {
        for (Command command : commands) {
            if (commandName == command.name) {
                return command
            }
        }
        throw new IllegalArgumentException("Failed to find command")
    }

    void addCommand(Command command) {
        this.commands.add(command)
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append("\nCommit:\n")
        for (Command cmd : commands) {
            sb.append(cmd.name).append("\n")
            for (Argument arg : cmd.arguments) {
                sb.append("   ").append(arg.keys).append(" => '").append(arg.getValue()).append("'\n")
            }
        }
        return sb.append("\n").toString()
    }
}
