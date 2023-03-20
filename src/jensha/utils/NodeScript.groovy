package jensha.utils
/**
 * NodeScripts are scripts which can be copied to nodes and executed there. They are stored in /resources/nodescript inside the
 * shared library and can contain arbitrary scripts in any language and any complexity. For that the directory is stashed
 * from the master and unstashed on the worker node.
 *
 * Note that you need to use Jenkins' native `unstash` function to copy the nodescript directory to the worker node.
 * This means you must execute it outside the script block of a stage.
 */
class NodeScript implements Serializable {

    private final PScript pscript
    private final String pathToNodeScriptDir
    public static final String NODE_SCRIPT_DIR = "nodescript"

    NodeScript(PScript pscript, String pathToNodeScriptDir) {
        this.pscript = pscript
        this.pathToNodeScriptDir = "${pathToNodeScriptDir}/resources/nodescript"
    }

    /**
     * Will create a file in ./nodescript at the specified relative subPath (e.g. ccli/secrets.json) with the specified content.
     * These files will be zipped as well and copied to the node, i.e. they will be available to your NodeScript.
     */
    void addFile(String subPath, String content) {
        String path = "${pathToNodeScriptDir}/${subPath}"
        this.pscript.sh """
cat <<EOF
cat > ${path} <<EOF\\n${content}\\nEOF
EOF
"""
        this.pscript.sh("cat > ${path} <<EOF\n${content}\nEOF")
    }

    void stash(String stashName) {
        this.pscript.sh("cp -r ${this.pathToNodeScriptDir} ${this.pscript.getEnvWorkspace()}")
        this.pscript.stash(stashName, "${NODE_SCRIPT_DIR}/**/*")
        this.pscript.sh("rm -r ${this.pscript.getEnvWorkspace()}/${NODE_SCRIPT_DIR}")
    }

}
