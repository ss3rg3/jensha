package jensha.jobs._directories

enum JobDir {

    jenkins_management("jenkins_management",
            "Jenkins Management",
            "Jobs to manage the Jenkins itself"),
    ccli("ccli",
            "CCLI",
            "Jobs using CCLI"),
    tryouts("tryouts",
            "Tryouts",
            "Jobs to try out stuff")



    // ------------------------------------------------------------------------------------------ //
    // ENUM IMPLEMENTATION
    // ------------------------------------------------------------------------------------------ //

    public final String folderName
    public final String displayName
    public final String description

    /**
     * @param folderName - Jenkins internal folder name
     * @param displayName - Name which you see in the Jenkins UI
     * @param description - Description to display in the UI
     */
    JobDir(String folderName, String displayName, String description) {
        this.folderName = folderName
        this.displayName = displayName
        this.description = description
    }

}
