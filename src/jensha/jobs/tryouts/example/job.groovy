package jensha.jobs.tryouts.example

import jensha.utils.PScript
import jensha.jobs._directories.JobDir
import jensha.utils.JobDslUtil
import jensha.utils.SharedLibrary
import jensha.utils.StageFlow

JobDslUtil jobDslUtil = new JobDslUtil(new PScript(this),
        SharedLibrary.getAnnotation(),
        JobDir.tryouts,
        "example",
        "Example")

pipelineJob(jobDslUtil.pathToPipelineJob) {
    displayName(jobDslUtil.displayName)
    description "Just some examples"

    logRotator {
        numToKeep(10)
    }

    parameters {
        stringParam('name', '', "String value, must be one of ${ExampleList.names}")
        stringParam('age', '', 'Integer value')
        choiceParam('option', JobDslUtil.toChoiceList(ExampleChoice.values()), 'String parameter')
        booleanParam('areYouSure', false, 'Boolean value')
        textParam('commitMessage', '', "${ExampleCLI.getHelpText()}")
        choiceParam("startFrom", JobDslUtil.toChoiceList(ExampleStage.values()),
                "Stage to start from. List is ordered.")
        choiceParam("stopAfter", StageFlow.getStopAfterStagesForJobDsl(ExampleStage.values()),
                "Stage to stop after, rest will be skipped.")
    }

    definition {
        cps {
            script(jobDslUtil.getComposedPipelineScript())
            sandbox()
        }
    }
}
