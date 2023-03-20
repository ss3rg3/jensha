package jensha.jobs.ccli.ccli

import jensha.jobs._directories.JobDir
import jensha.utils.JobDslUtil
import jensha.utils.PScript
import jensha.utils.SharedLibrary

JobDslUtil jobDslUtil = new JobDslUtil(new PScript(this),
        SharedLibrary.getAnnotation(),
        JobDir.ccli,
        "ccli",
        "uc")

pipelineJob(jobDslUtil.pathToPipelineJob) {
    displayName(jobDslUtil.displayName)
    description "CCLI wrapper for https://github.com/ellzee/ccli_uc.git"

    logRotator {
        numToKeep(10)
    }

    parameters {
        stringParam("clusterRepoUrl", "https://github.com/ellzee/ccli_uc.git", "URL the of cluster repo.")
        textParam("buildCommands", "", "List of CCLI build commands, just the parameters. E.g. ` --match \"jenkins.*\" --subpath jenkins --push`.")
    }

    definition {
        cps {
            script(jobDslUtil.getComposedPipelineScript())
            sandbox()
        }
    }
}
