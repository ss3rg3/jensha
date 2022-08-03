package jensha.pipelinecli

import jensha.utils.PScript

import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern

class CommitCLI {

    private List<Command> commands = new ArrayList<>()
    private PScript pscript = null
    private String help = null

    CommitCLI() {

    }

    CommitCLI(PScript pscript) {
        // Don't call init() from constructor, because Jenkins quirks
        this.pscript = pscript
        for (Command command : commands) {
            command.setPScript(pscript)
        }
    }

    void init(PScript pscript) {
        this.pscript = pscript
        for (Command command : commands) {
            command.setPScript(pscript)
        }
    }

    CommitCLI help(String helpText) {
        this.help = helpText
        return this
    }

    CommitCLI addCommand(Command command) {
        this.commands.add(command)
        return this
    }

    Commit parse(String commitMessage) {
        if(this.pscript == null) {
            throw new IllegalStateException("CommitCLI not initialized, use init() beforehand")
        }

        String[] lines = commitMessage.split("\n")
        List<List<String>> tokenizedCommands = new ArrayList<>()
        for (String line : lines) {
            if (line.startsWith("/")) {
                tokenizedCommands.add(tokenize(line))
            }
        }

        Commit commit = new Commit()
        for (List<String> tokenizedCommand : tokenizedCommands) {

            int currentIndex = 0
            Command commitCommand = null
            Command specCommand = null

            while (currentIndex < tokenizedCommand.size()) {
                String currentToken = tokenizedCommand[currentIndex]

                // Command name
                if (currentIndex == 0) {
                    if (hasCommand(currentToken)) {
                        commitCommand = new Command(currentToken)
                        specCommand = getCommand(currentToken)
                    } else {
                        pscript.exitWithError("Command in commit not recognized, check '/${currentToken}'")
                    }
                }

                // Arguments
                if (currentIndex > 0) {
                    Argument argument = specCommand.requireArgument(currentToken)
                    if (argument instanceof Parameter) {
                        Parameter parameter = (Parameter) argument
                        currentIndex++
                        addParameter(tokenizedCommand, currentIndex, currentToken, parameter, commitCommand)

                    } else if (argument instanceof Flag) {
                        Flag flag = (Flag) argument
                        flag.setToTrue()
                        commitCommand.addFlag(flag)
                    } else {
                        pscript.exitWithError("Type of Argument not recognized, got ${argument.class}")
                    }
                }

                currentIndex++
            }
            commit.addCommand(commitCommand)
        }

        return commit
    }

    private void addParameter(List<String> tokenizedCommand,
                              int currentIndex,
                              String currentToken,
                              Parameter parameter,
                              Command commitCommand) {
        String nextToken = tokenizedCommand[currentIndex]
        if (nextToken.startsWith("-")) {
            pscript.exitWithError("Value of parameter must not start with '-', got `${currentToken} ${nextToken}`")
        }
        parameter.setValue(nextToken)
        commitCommand.addParam(parameter)
    }

    private static tokenize(String rawCommand) {
        List<String> tokens = new ArrayList<>()
        Pattern pattern = Pattern.compile('^"|"$')
        for (String token : Tokenizer.tokenize(rawCommand.substring(1), true)) {
            tokens.add(pattern.matcher(token).replaceAll(""))
        }
        return tokens
    }

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
        pscript.exitWithError("Failed to find command")
        throw new IllegalStateException("Unreachable code")
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder()

        if(this.help != null) {
            sb.append(this.help).append("\n\n")
        }

        AtomicInteger maxLength = new AtomicInteger()
        commands.each {command ->
            for (Argument arg : command.arguments) {
                if (arg.keys.toString().length() > maxLength.get()) {
                    maxLength.set(arg.keys.toString().length())
                }
            }
        }

        commands.each {command ->
            sb.append("/").append(command.name).append("\n")
            for (Argument arg : command.arguments) {
                String varSpaces = spaces(variableLength(arg, maxLength.get()) + 4)
                sb.append(spaces(4)).append(arg.keys).append(varSpaces).append(getType(arg)).append(spaces(4)).append(arg.help).append("\n")
            }
        }

        return sb.toString()
    }

    static int variableLength(Argument arg, int maxLength) {
        return maxLength - arg.keys.toString().length()
    }

    static String spaces(int spaces) {
        int i = 0
        StringBuilder sb = new StringBuilder()
        while (i < spaces) {
            sb.append(" ")
            i++
        }
        return sb.toString()
    }

    String getType(Argument argument) {
        if (argument instanceof Flag) {
            return "Flag "
        }
        if (argument instanceof Parameter) {
            return "Param"
        }
        pscript.exitWithError("Failed to recognize type of Argument")
        throw new IllegalStateException("Unreachable code")
    }
}
