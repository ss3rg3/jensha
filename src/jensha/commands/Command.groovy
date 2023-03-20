package jensha.commands

/**
 * Builder for commands. Doesn't execute, only returns the desired command as string
 */
class Command {

    protected List<String> command = new ArrayList<>()

    /**
     * First fragment of the command. Mostly some CLI tool name or executor, e.g. `python ccli --debug`.
     * This overwrites the first command fragment in the command list. So you can use it any time, even after having used add()
     */
    Command setBaseCommand(String baseCommand) {
        if (command.isEmpty()) {
            this.command.add(baseCommand)
        } else {
            this.command.set(0, baseCommand)
        }
        return this
    }

    Command add(String commandFragment) {
        this.command.add(commandFragment)
        return this
    }

    /**
     * Returns the full command
     */
    String get() {
        return command.join(" ")
    }

}
