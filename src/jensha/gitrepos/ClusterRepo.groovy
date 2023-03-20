package jensha.gitrepos

import jensha.constants.Constants
import jensha.utils.PScript

class ClusterRepo extends GitRepo {

    ClusterRepo(PScript pscript, String remoteRepositoryUrl, String userColonToken) {
        super(pscript,
                "${Constants.REPO_DIR}/clusters",
                remoteRepositoryUrl,
                userColonToken)

    }

}
