package jensha.utils

import jensha.constants.Constants
import jensha.jobs._directories.JobDir

/**
 * Helper for Job DSL job definitions. Provides all required variables in a standardized way.
 */
class JobDslUtil {

    private final PScript pscript
    private final String sharedLibraryAnnotation

    public final JobDir jobDirectory

    /**
     * Name displayed in Jenkins UI
     */
    public final String displayName

    /**
     * Value for readFileFromWorkspace() in your job.groovy (Job DSL config)
     * Concatenation of "${Constants.JOBS_FOLDER}/${jobDirectory.path}/${sharedLibraryFolderName}/pipeline.groovy"
     * E.g: src/jenkins/jobs/utils/check_backups/pipeline.groovy
     */
    public final String pathToJenkinsFile

    /**
     * Value for pipelineJob() in your job.groovy (Job DSL config)
     * Normalized display name to something Jenkins friendly, e.g. "Create Tenant" => "create_tenant"
     * Result is combo of job folder + job name, e.g. "provisioning/create_tenant"
     */
    public final String pathToPipelineJob

    /**
     * Same as the other one, but you can specify the file name of your JenkinsFile
     * @param jenkinsFileName - name of the JenkinsFile in the jobDirectoryInSharedLib
     */
    JobDslUtil(PScript pscript,
               String sharedLibraryAnnotation,
               JobDir jobDirectory,
               String jobDirectoryInSharedLib,
               String displayName,
               String jenkinsFileName) {
        this.pscript = pscript
        this.sharedLibraryAnnotation = sharedLibraryAnnotation
        this.jobDirectory = jobDirectory
        this.displayName = displayName
        this.pathToJenkinsFile = "${Constants.JOBS_DIRECTORY}/${jobDirectory.folderName}/${jobDirectoryInSharedLib}/${jenkinsFileName}"
        String jenkinsInternalJobName = displayName
                .replaceAll("[^\\w]+", "_") // everything that is not a-Z0-9
                .replaceAll('^[_]|[_]$', "") // possible leftover underscores at the start or end of the name
                .toLowerCase()
        this.pathToPipelineJob = "${jobDirectory.folderName}/${jenkinsInternalJobName}"
    }

    /**
     * @param script - The Job DSL script itself (just pass `new PScript(this)`)
     * @param sharedLibraryAnnotation - @Library annotation, use helper SharedLibrary
     * @param jobDirectory - see JobDirectory enum. These are the folders in the Jenkins UI which categorize the jobs
     * @param jobDirectoryInSharedLib - directory of the job in the shared library where the job.groovy + JenkinsFile are
     * @param displayName - of the directory in the Jenkins UI, can have whitespaces and special characters
     */
    JobDslUtil(PScript pscript,
               String sharedLibraryAnnotation,
               JobDir jobDirectory,
               String jobDirectoryInSharedLib,
               String displayName) {
        this(pscript, sharedLibraryAnnotation, jobDirectory, jobDirectoryInSharedLib, displayName, "JenkinsFile")
    }

    /**
     * Loads the JenkinsFile behind pathToJenkinsFile as string
     */
    String getJenkinsFile() {
        return "${this.pscript.readFileFromWorkspace(this.pathToJenkinsFile)}"
    }

    /**
     * This just puts the @Library annotation together with the pipeline script string (JenkinsFile).
     * It also removes the Groovy `package` definition in case there is one, which throws an error if it stays
     */
    String getComposedPipelineScript() {
        String composition = sharedLibraryAnnotation.trim()
        composition += "\n\n"
        composition += this.getJenkinsFile().replaceAll("^package.*", "")
        println composition
        return composition
    }

    static List<Object> toChoiceList(Enum[] enumValues) {
        return List.of(enumValues)
    }

}
