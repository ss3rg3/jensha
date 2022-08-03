package jensha.utils

class StageFlow<T extends Enum> {

    private final PScript pscript

    private T startFrom = null
    private T stopAfter = null
    private boolean isStarted
    private boolean isStopped

    StageFlow(PScript pscript) {
        this.pscript = pscript
    }

    void fromTo(T startFrom, T stopAfter) {
        this.startFrom = startFrom
        this.stopAfter = stopAfter
    }

    /**
     * Returns true if specified stage has been reached or succeeded
     * Returns false if the stop stage has been passed
     */
    boolean shouldStartHere(T stage) {
        if (startFrom == null || stopAfter == null) {
            pscript.exitWithError("Neither startFrom & stopAfter stage must be null")
        }

        if (isStopped) {
            return false
        }

        if (isStarted) {
            return true
        }

        if (startFrom == stage) {
            isStarted = true
            return true
        }

        return false
    }

    /**
     * Returns true until specified stages hasn't been passed
     */
    boolean shouldStop(T stage) {
        if (startFrom == null || stopAfter == null) {
            pscript.exitWithError("Neither startFrom & stopAfter stage must be null")
        }

        if (isStopped) {
            return true
        }

        if (stopAfter == stage) {
            isStopped = true
            return false
        }

        return false
    }

    /**
     * Evaluates shouldStartHere() & shouldStop() together, so you don't need to evaluate each one separately
     */
    boolean shouldProcess(T stage) {
        return shouldStartHere(stage) && !shouldStop(stage)
    }

    static List<Enum> getStopAfterStagesForJobDsl(Enum[] enumValues) {
        List<Enum> list = new ArrayList<>()
        list.add(enumValues[enumValues.length-1])
        list.addAll(enumValues)
        return list
    }
}
