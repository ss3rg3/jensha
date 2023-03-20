package jensha.pipelinecli

import jensha.utils.PScript

class Command {

    public final String name
    public final List<Argument> arguments = new ArrayList<>()
    private PScript pscript = null

    Command(String name) {
        this.name = name
    }

    void setPScript(PScript pscript) {
        this.pscript = pscript
    }

    Argument requireArgument(String key) {
        for (Argument argument : arguments) {
            for (String candidateKey : argument.keys) {
                if (key == candidateKey) {
                    return argument
                }
            }
        }
        pscript.exitWithError("Failed to find matching required argument for key '${key}' for command '${this.name}'")
        throw new IllegalStateException("Unreachable code")
    }

    Argument requireArgument(List<String> keys) {
        return requireArgument(keys.get(0))
    }

    /**
     * If parameter is not found then NULL is returned
     */
    Parameter getParameter(String key) {
        for (Argument argument : arguments) {
            for (String candidateKey : argument.keys) {
                if (key == candidateKey) {
                    return requireParameter(key)
                }
            }
        }
        return null
    }

    Parameter getParameter(List<String> keys) {
        return getParameter(keys.get(0))
    }

    boolean hasArgument(String key) {
        for (Argument argument : arguments) {
            for (String candidateKey : argument.keys) {
                if (key == candidateKey) {
                    return true
                }
            }
        }
        return false
    }

    boolean hasArgument(List<String> keys) {
        return hasArgument(keys.get(0))
    }

    boolean hasFlag(List<String> keys) {
        return hasArgument(keys.get(0))
    }

    boolean hasFlag(String key) {
        return hasArgument(key)
    }

    boolean hasParameter(List<String> keys) {
        return hasArgument(keys.get(0))
    }

    boolean hasParameter(String key) {
        return hasArgument(key)
    }

    Parameter requireParameter(String key) {
        Argument argument = requireArgument(key)
        if (!argument instanceof Parameter) {
            pscript.exitWithError("Failed to find matching required parameter for key '${key}' for command '${this.name}'")
        }
        return (Parameter) argument
    }

    Parameter requireParameter(List<String> keys) {
        return requireParameter(keys.get(0))
    }

    Flag requireFlag(String key) {
        Argument argument = requireArgument(key)
        if (!argument instanceof Flag) {
            pscript.exitWithError("Failed to find matching required flag for key '${key}' for command '${this.name}'")
        }
        return (Flag) argument
    }

    Flag requireFlag(List<String> keys) {
        return requireFlag(keys.get(0))
    }



    /**
     * Add a parameter, for string values.
     * If the parameter is set, then it must be followed by a value, e.g. --match jenkins-0
     */
    Command addParam(List<String> keys, String help) {
        arguments.add(new Parameter(keys, help))
        return this
    }

    Command addParam(Parameter parameter) {
        arguments.add(parameter)
        return this
    }

    /**
     * Add a flag, for boolean values.
     * True if flag is set, false if it's missing.
     */
    Command addFlag(List<String> keys, String help) {
        arguments.add(new Flag(keys, help))
        return this
    }

    Command addFlag(Flag flag) {
        arguments.add(flag)
        return this
    }
}
