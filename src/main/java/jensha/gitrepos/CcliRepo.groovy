package jensha.gitrepos

import jensha.commands.CcliCommand
import jensha.constants.Constants
import jensha.utils.PScript

class CcliRepo extends GitRepo {

    private static final String remoteRepositoryUrl = "https://github.com/surg3/ccli.git"

    CcliRepo(PScript pscript, String userColonToken) {
        super(pscript,
                "${Constants.REPO_DIR}",
                remoteRepositoryUrl,
                userColonToken)
    }

    void cloneAndInstall() {
        this.pullOrClone()
        this.pscript.sh("cd ${this.repositoryPath} && pip3 install .")
    }

    void execute(CcliCommand ccliCommand) {
        this.pscript.sh("cd ${this.repositoryPath} && ${ccliCommand.get()}")
    }

}
