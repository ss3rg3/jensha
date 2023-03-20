package jensha.gitrepos

import jensha.utils.PScript

class GitRepo implements Serializable {

    final PScript pscript
    final String repositoryPath
    final String repositoryName
    final String remoteRepositoryUrl
    final String remoteRepositoryUrlClonable

    /**
     * Git repository which is stored on the Jenkins master, can be a git@ or https:// repo
     *
     * @param repositoryParentPath - parent directory where to clone into (repositoryPath = parent + repo name)
     * @param remoteRepositoryUrl - clean URL which can displayed in job summary, no secrets!
     * @param userColonToken - will be rendered into the URL for cloning, i.e. `user:token@github.com/org/repo_name`
     */
    GitRepo(PScript pscript, String repositoryParentPath, String remoteRepositoryUrl, String userColonToken) {
        if (!remoteRepositoryUrl.startsWith("https://")) {
            pscript.exitWithError("Constructor needs a https:// Git repo. Other URL work with SSH key, not 'userColonToken'")
        }

        this.pscript = pscript

        this.remoteRepositoryUrl = remoteRepositoryUrl
        this.remoteRepositoryUrlClonable = remoteRepositoryUrl.replaceFirst("//", "//${userColonToken}@")

        this.repositoryName = remoteRepositoryUrl.replaceAll("^.*/|\\.git\$", "")
        this.repositoryPath = "${repositoryParentPath.replaceFirst("^~", System.getProperty("user.home"))}/${this.repositoryName}"
    }

    boolean doesRepositoryPathExist() {
        return new File(repositoryPath).exists()
    }

    /**
     * Clone the repo. If repositoryPath already exists then delete it beforehand
     */
    void cloneRepo() {
        if (new File(repositoryPath).exists()) {
            String cmd = "rm -r ${repositoryPath}"
            pscript.execute(cmd,
                    "Repository existed already, deleted `${repositoryPath}`<br>${cmd}",
                    "Failed to delete existing repository `${repositoryPath}` before cloning<br>${cmd}")
        }
        String cmd = "git clone ${remoteRepositoryUrlClonable} ${repositoryPath}"
        pscript.execute(cmd,
                "Cloned repository into `${repositoryPath}`<br>git clone ${remoteRepositoryUrl} ${repositoryPath}",
                "Failed clone repository into `${repositoryPath}`<br>git clone ${remoteRepositoryUrl} ${repositoryPath}")
    }

    void switchToBranch(String branchName) {
        String cmd = "(cd ${repositoryPath} && git checkout ${branchName})"
        pscript.execute(cmd,
                "Switched branch in `${repositoryPath}` to `${branchName}`<br>${cmd}",
                "Failed to switch branch in `${repositoryPath}` to `${branchName}`<br>${cmd}")
    }

    /**
     * Runs `git clean -fdx`
     */
    void cleanFdx() {
        String cmd = "(cd ${repositoryPath} && git clean -fdx > /dev/null)" // output suppressed!
        pscript.execute(cmd,
                "Cleaned repository `${repositoryPath}`<br>${cmd}",
                "Failed to clean repository `${repositoryPath}`<br>${cmd}")
    }

    /**
     * Run `git pull --all`
     */
    void pull() {
        try {
            String cmd = "(cd ${this.repositoryPath} && git pull --all)"
            pscript.sh("${cmd}")
            pscript.logSuccess("Pulled repository `${this.repositoryPath}`<br>${cmd}")
        } catch(Exception e) {
            String cmd = "(cd ${this.repositoryPath} && git pull --rebase)"
            pscript.execute(cmd,
                    "Pulled repository `${this.repositoryPath}`<br>${cmd}",
                    "Failed to pulled repository `${this.repositoryPath}`<br>${cmd}")
        }
    }

    /**
     * If the repo directory exists then it will pulled, if not then cloned. If pulling fails then it will be deleted and cloned.
     */
    void pullOrClone() {
        if(doesRepositoryPathExist()) {
            try {
                pull()
            } catch (Exception e) {
                pscript.logWarning("Will reclone repository because: ${e.message}")
                cloneRepo()
            }
        } else {
            cloneRepo()
        }
    }

}
