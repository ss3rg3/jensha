package jensha.pipelinecli

abstract class Argument<T> {

    public final List<String> keys
    public final String help

    Argument(List<String> keys, String help) {
        this.keys = keys
        this.help = help

        if (keys == null || keys.isEmpty()) {
            throw new IllegalStateException("'keys' must not be null or empty")
        }

        if (help == null) {
            throw new IllegalStateException("'help' must not be null")
        }
    }

    abstract T getValue()
}
