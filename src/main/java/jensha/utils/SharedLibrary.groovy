package jensha.utils

import jensha.constants.Constants

/**
 * Helper class for generating @Library annotations for pipeline DSL and locating the shared library
 */
class SharedLibrary {

    static String getAnnotation(String sharedLibName, String branch) {
        if(branch == null) {
            return "@Library('${sharedLibName}') _"
        }
        return "@Library('${sharedLibName}@${branch}') _"
    }

    /**
     * Creates the standard shared lib annotation, with Constants.SHARED_LIB_NAME and no branch
     */
    static String getAnnotation() {
        return getAnnotation(Constants.SHARED_LIB_NAME, null)
    }

    /**
     * Creates the standard shared lib annotation, with JobDslScript.SHARED_LIB_NAME and with specified branch
     */
    static String getAnnotation(String branch) {
        return getAnnotation(Constants.SHARED_LIB_NAME, branch)
    }

    /**
     * Returns the absolute path to the shared library clone on the file system, wherever it is loaded.
     * Usually it's in `env.WORKSPACE + "@libs/" + nameOfSharedLib` but with concurrent jobs this changes.
     * # todo check Jenkins folder structure again. I swear I saw the shared lib in every JOB folder, not WORKSPACE.
     */
    static String getPathToSharedLibrary() {
        // todo doesn't work. On Jenkins this returns the home folder of the Jenkins
        throw new Exception("doesn't work. On Jenkins this returns the home folder of the Jenkins")
//        return new File("").getAbsolutePath()
    }

}
