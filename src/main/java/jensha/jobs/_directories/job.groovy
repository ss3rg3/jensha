package jensha.jobs._directories

/**
 * Instructs Jenkins to create all specified folders in JobFolders. This must run first in your seed job.
 */
JobDir.values().each { jobDir ->
    folder(jobDir.folderName) {
        displayName(jobDir.displayName)
        description(jobDir.description)
    }
}
