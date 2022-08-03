import groovy.json.JsonOutput
import groovy.transform.Field
import jensha.gitrepos.GitRepo
import jensha.jobs.tryouts.example.ExampleCLI
import jensha.jobs.tryouts.example.ExampleStage
import jensha.pipelinecli.Command
import jensha.pipelinecli.Commit
import jensha.pipelinecli.CommitCLI
import jensha.utils.NodeScript
import jensha.constants.Result
import jensha.httpclient.HttpClient
import jensha.httpclient.HttpResponse
import jensha.jobs.tryouts.example.ExampleChoice
import jensha.jobs.tryouts.example.ExampleList
import jensha.params.Param
import jensha.utils.AssertThat
import jensha.utils.PScript
import jensha.utils.StageFlow

// Params
@Field String name
@Field Integer age
@Field ExampleChoice option
@Field Boolean areYouSure
@Field ExampleStage startFrom
@Field ExampleStage stopAfter

// Utils
@Field PScript pscript = new PScript(this)
@Field Param param = new Param(pscript)
@Field AssertThat assertThat = new AssertThat(pscript)
@Field CommitCLI CLI
@Field StageFlow<ExampleStage> flow = new StageFlow<>(pscript)

def call() {

    pipeline {
        agent any

        environment {
            ECHO_SCRIPT = "python3 ${NodeScript.NODE_SCRIPT_DIR}/ccli/echo.py"
        }

        stages {

            stage('Initialize') {
                steps {
                    script {

                        // Init CLI
                        CLI = ExampleCLI.create(pscript)

                        // Set build name
                        pscript.setNameOfBuild("Example #${env.BUILD_ID}")

                        // Params
                        name = param.asString(params.name, "name")
                                .isNotEmpty()
                                .isOneOf(ExampleList.names)
                                .get()
                        age = param.asInteger(params.age, "age")
                                .isGreaterThan(10)
                                .get()
                        option = (ExampleChoice) param.asEnum(params.option, ExampleChoice.class, "option")
                                .get()
                        areYouSure = param.asBoolean(params.areYouSure, "areYouSure")
                                .mustBeTrue()
                                .get()
                        startFrom = (ExampleStage) param.asEnum(params.startFrom, ExampleStage.class, "startFrom")
                                .get()
                        stopAfter = (ExampleStage) param.asEnum(params.stopAfter, ExampleStage.class, "stopAfter")
                                .get()

                        // Init StageFlow
                        flow.fromTo(startFrom, stopAfter)

                        // Some more asserts
                        assertThat.isNotEmpty(name, "'name' must not be null")
                        assertThat.isNotNull(option, "'option' must not be null")

                        // Report
                        pscript.logSuccess("<h3>Params</h3>" +
                                "Name: '${name}'<br>" +
                                "Age: '${age}'<br>" +
                                "Option: '${option}'<br>" +
                                "areYouSure: '${areYouSure}'<br>"
                        )
                    }
                }
            }


            stage('Execute commands') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.executeCommands) }
                    }
                }
                steps {
                    // "script" blocks will always be executed on the Jenkins MASTER
                    script {
                        String cmd = "echo 'CURRENT PATH:' && pwd"
                        pscript.execute(cmd,
                                "Successfully executed command:<br>\n${cmd}",
                                "Failed to execute command:<br>\n${cmd}")

                        cmd = "ls -la ${pscript.getPathToSharedLibrary()}"
                        pscript.execute(cmd,
                                "Successfully executed command:<br>\n${cmd}",
                                "Failed to execute command:<br>\n${cmd}")

                        env.commandFromScriptBlock = "pwd && ls -la" // Reusing `cmd` from above wouldn't work
                    }
                    // Commands outside of "script" blocks will be executed on the BUILD NODE (might be the master itself)
                    sh "${env.commandFromScriptBlock}"
                }
            }


            stage('Clone Git repo') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.cloneGitRepo) }
                    }
                }
                steps {
                    script {
                        String userColonPassword = userColonPassword = pscript.getUserColonPassword("uc_repo_user")
                        GitRepo gitRepo = new GitRepo(pscript,
                                pscript.envWorkspace,                   // parent directory where to clone the repo into
                                "https://github.com/53rg3/jekylldocs",  // URL of the repo
                                userColonPassword)                      // user:password
                        gitRepo.cloneRepo()

                        sh "ls -la ${gitRepo.repositoryPath}"
                    }
                }
            }


            stage('NodeScript') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.nodeScript) }
                    }
                }
                steps {
                    // Prepare your NodeScript on the master and stash it
                    script {
                        sh "echo 'INIT NODESCRIPT:'"
                        NodeScript nodeScript = new NodeScript(pscript, pscript.getPathToSharedLibrary())
                        sh "echo 'ADD FILE:'"
                        String content = """
                            asdf
                            yxcv
                            qwer
                        """.stripIndent().trim()
                        nodeScript.addFile("ccli/secrets.json", JsonOutput.toJson([
                                "user"       : "bob",
                                "token"      : "super_secret",
                                "secret_file": content
                        ]))
                        sh "echo 'STASH:'"
                        nodeScript.stash(NodeScript.NODE_SCRIPT_DIR)
                    }
                    // Unstash and execute on the worker node (might be the master)
                    unstash NodeScript.NODE_SCRIPT_DIR
                    sh "${env.ECHO_SCRIPT}"
                }
            }


            stage('HTTP Requests') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.httpRequests) }
                    }
                }
                steps {
                    script {
                        HttpResponse httpResponse = HttpClient.get(pscript, "https://example.com")
                        pscript.print(httpResponse.toString())
                    }
                }
            }


            stage('PipelineCLI') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.pipelineCli) }
                    }
                }
                steps {
                    script {
                        String commitMessage = param.asString(params.commitMessage, "commitMessage").get()
                        if (commitMessage == null) {
                            pscript.logWarning("SKIPPING stage '${pscript.stageName}' because no commitMessage provided")
                            pscript.markStageAsUnstable()
                            return
                        }

                        Commit commit = CLI.parse(commitMessage)

                        if (commit.hasCommand(ExampleCLI.ANSIBLE)) {
                            Command ansibleCmd = commit.getCommand(ExampleCLI.ANSIBLE)

                            String match = ansibleCmd.requireParameter(ExampleCLI.matchParam).getValue()
                            pscript.logSuccess("/ansible command provided with --match=${match}")

                            String playbooks = ansibleCmd.getParameter(ExampleCLI.matchParam).getValue()
                            if (playbooks != null) {
                                pscript.logSuccess("/ansible command provided with --playbooks=${playbooks}")
                            } else {
                                pscript.logWarning("/ansible command NOT provided with --playbooks")
                            }

                            boolean usePublicIP = ansibleCmd.hasFlag(ExampleCLI.usePublicIpFlag)
                            if (usePublicIP) {
                                pscript.logSuccess("/ansible command provided with --use-public-ip")
                            } else {
                                pscript.logWarning("/ansible command WITHOUT --use-public-ip")
                            }

                        }

                    }
                }
            }


            stage('Unstable stage') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.unstableStage) }
                    }
                }
                steps {
                    script {
                        pscript.markStageAsUnstable("Something fishy happened") // With reason
                        pscript.markBuildAsUnstable()                           // Without reason
                    }
                }
            }


            stage('Failing stage') {
                when {
                    allOf() {
                        expression { flow.shouldProcess(ExampleStage.failingStage) }
                    }
                }
                steps {
                    script {
                        // catching the error with buildResult == UNSTABLE, or otherwise the whole job is considered failed
                        catchError(stageResult: Result.FAILURE, buildResult: Result.UNSTABLE) {
                            pscript.exitWithError("<b>Stage: '${pscript.getStageName()}':<b> Just some error to show some error. All good actually.")
                        }
                    }
                }
            }


        }


        post {
            always {
                script {
                    pscript.logSuccess("Post action 'always' was successful")
                }
            }
            success {
                script {
                    pscript.logSuccess("Post action 'success': Triggered because the job succeeded.")
                }
            }
            failure {
                script {
                    pscript.logWarning("Post action 'failure': Triggered because the job failed.")
                }
            }
        }


    }
}
