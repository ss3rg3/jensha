package jensha.utils

import jensha.constants.Constants

/**
 * Wrapper for Pipeline Script to avoid CSP exceptions
 */
class PScript implements Serializable {

    private final Script script
    private static String RED = "\033[1;31m"
    private static String YELLOW = "\033[1;33m"
    private static String GREEN = "\033[1;32m"
    private static String END = "\033[0m"

    PScript(Script script) {
        this.script = script
    }


    // ------------------------------------------------------------------------------------------ //
    // PIPELINE
    // ------------------------------------------------------------------------------------------ //

    void sh(String command) {
        this.script.sh(command)
    }

    String shGetOutput(String command) {
        return this.script.sh(returnStdout: true, script: "${command}")
    }

    String readFileFromWorkspace(String path) {
        return this.script.readFileFromWorkspace(path)
    }

    String getEnvWorkspace() {
        return this.script.env.WORKSPACE
    }

    String getStageName() {
        return this.script.env.STAGE_NAME
    }

    // ------------------------------------------------------------------------------------------ //
    // LOGGING
    // ------------------------------------------------------------------------------------------ //

    void logSuccess(String message) {
        this.script.ansiColor('xterm') {
            this.script.sh """
            set +x
            cat << EOF
${GREEN}SUCCESS: ${message}${END}
EOF
        """
        }
        this.script.manager.createSummary("green.gif").appendText("${message}", false)
    }

    void logWarning(String message) {
        this.script.ansiColor('xterm') {
            this.script.sh """
            set +x
            cat << EOF
${YELLOW}WARNING: ${message}${END}
EOF
        """
        }
        this.script.manager.createSummary("warning.gif").appendText("${message}", false)
    }

    void logError(String message) {
        this.script.ansiColor('xterm') {
            this.script.sh """
            set +x
            cat << EOF
${RED}ERROR: ${message}${END}
EOF
        """
        }
        this.script.manager.createSummary("error.gif").appendText("${message}", false)
    }

    /**
     * Allows to output multi line texts
     */
    void print(String multiLineText) {
        this.script.sh """
        set +x
        cat << EOF
${multiLineText}
EOF
    """
    }


    // ------------------------------------------------------------------------------------------ //
    // EXIT
    // ------------------------------------------------------------------------------------------ //

    void exitWithError(String message) {
        this.logError(message)
        throw new IllegalStateException(message)
    }

    void exitWithError(Exception e) {
        this.logError("Exception message: " + e.message)
        throw new IllegalStateException(e)
    }

    void exitWithError(String message, Exception e) {
        this.logError("${message}<br>" + e.message)
        throw new IllegalStateException(e)
    }


    // ------------------------------------------------------------------------------------------ //
    // OTHER UTILS
    // ------------------------------------------------------------------------------------------ //

    void setNameOfBuild(String nameOfBuild) {
        this.script.buildName(nameOfBuild)
    }

    /**
     * todo experimental, rewrite to get dir name without knowing the hash beforehand
     * Jenkins stores the shared lib inside the job folder, but uses some hash name the folder of the shared lib
     * Hash seems to be consistent, therefore this quick hack. There's also a file called `HASH-name.txt` which contains
     * the name of the lib
     */
    String getPathToSharedLibrary() {
        String sharedLibDir = this.shGetOutput("grep -s '${Constants.SHARED_LIB_NAME}' /var/jenkins_home/jobs/tryouts/jobs/example/workspace/../builds/${this.script.env.BUILD_ID}/libs/* | sed 's/\\/.*\\///g' | sed 's/-name.*//g'")
        return "${this.script.env.WORKSPACE}/../builds/${this.script.env.BUILD_ID}/libs/${sharedLibDir.trim()}"
    }

    /**
     * Execute a shell command. Output will be logged to console.
     */
    void execute(String command, String successMessage, String failureMessage) {
        try {
            this.script.sh(command)
            this.logSuccess(successMessage)
        } catch (Exception e) {
            this.logError(failureMessage)
            throw new IllegalStateException(failureMessage, e)
        }
    }

    /**
     * Execute a shell command. Output will NOT be logged to console, but returned as variable
     */
    String executeGetOutput(String command, String successMessage, String failureMessage) {
        try {
            String output = this.script.sh(returnStdout: true, script: command)
            this.logSuccess(successMessage)
            return output
        } catch (Exception e) {
            this.logError(failureMessage)
            throw new IllegalStateException(failureMessage, e)
        }
    }

    /**
     * Gets the current stage name
     */
    String currentStage() {
        return this.script.env.STAGE_NAME
    }

    /**
     * Makes a stage yellow. For failure throw an error, for success do nothing
     */
    void markStageAsUnstable(String reason = "No reason given") {
        this.logWarning("<b>Stage unstable because '${this.currentStage()}':</b> ${reason}")
        this.script.unstable(reason)
    }

    /**
     * Makes the build yellow. For failure throw an error, for success do nothing
     */
    void markBuildAsUnstable(String reason = "No reason given") {
        this.logWarning("<b>Build unstable because '${this.currentStage()}':</b> ${reason}")
        this.script.currentBuild.result = "UNSTABLE"
    }

    /**
     * Returns user + password behind desired credentials ID
     * Format: `your_user:your_password`
     */
    String getUserColonPassword(String credentialsId) {
        this.script.withCredentials([script.usernameColonPassword(credentialsId: "${credentialsId}", variable: 'userColonPassword')]) {
            return script.env.userColonPassword
        }
    }

    /**
     * Returns secret text credentials (single line string, e.g. an API key) behind desired credentials ID
     */
    String getSingleLineSecret(String credentialsId) {
        this.script.withCredentials([script.string(credentialsId: "${credentialsId}", variable: 'singleLineString')]) {
            return script.env.singleLineString
        }
    }

    /**
     * Wrapper for stash, see https://www.jenkins.io/doc/pipeline/examples/#unstash-different-dir
     */
    void stash(String stashName, String includes) {
        this.script.stash(name: stashName, includes: includes)
    }

}
