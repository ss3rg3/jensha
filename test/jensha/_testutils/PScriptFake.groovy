package jensha._testutils

import jensha.utils.PScript


/**
 * Wrapper for Pipeline Script to avoid CSP exceptions
 */
class PScriptFake extends PScript implements Serializable {

    private final Script script
    private static String RED = "\033[1;31m"
    private static String YELLOW = "\033[1;33m"
    private static String GREEN = "\033[1;32m"
    private static String END = "\033[0m"

    PScriptFake() {
        super(null)
        this.script = null
    }
// ------------------------------------------------------------------------------------------ //
    // PIPELINE
    // ------------------------------------------------------------------------------------------ //

    void sh(String command) {
        throw new IllegalStateException("Requires Jenkins pipeline script. Can't be used for testing.")
    }

    String readFileFromWorkspace(String path) {
        throw new IllegalStateException("Requires Jenkins pipeline script. Can't be used for testing.")
    }


    // ------------------------------------------------------------------------------------------ //
    // LOGGING
    // ------------------------------------------------------------------------------------------ //

    @Override
    void logSuccess(String message) {
        println "${GREEN}SUCCESS: ${message}${END}"
    }

    @Override
    void logWarning(String message) {
        println "${YELLOW}WARNING: ${message}${END}"
    }

    @Override
    void logError(String message) {
        println "${RED}ERROR: ${message}${END}"
    }

    /**
     * Allows to output multi line texts
     */
    void print(String message) {
        println "${message}"
    }


    // ------------------------------------------------------------------------------------------ //
    // MISC HELPERS
    // ------------------------------------------------------------------------------------------ //

    void setNameOfBuild(String nameOfBuild) {
        throw new IllegalStateException("Requires Jenkins plugin. Can't be used for testing.")
    }
}
