import groovy.transform.Field
import jensha.commands.CcliCommand
import jensha.gitrepos.CcliRepo
import jensha.gitrepos.ClusterRepo
import jensha.params.Param
import jensha.utils.AssertThat
import jensha.utils.PScript

// Params
@Field String clusterRepoUrl
@Field String buildCommands

// Utils
@Field PScript pscript = new PScript(this)
@Field Param param = new Param(pscript)
@Field AssertThat assertThat = new AssertThat(pscript)
@Field String userColonPassword
@Field ClusterRepo clusterRepo
@Field CcliRepo ccliRepo

def call() {

    pipeline {
        agent any

        stages {

            stage('Initialize') {
                steps {
                    script {
                        clusterRepoUrl = param.asString(params.clusterRepoUrl, "clusterRepo")
                                .isNotEmpty()
                                .get()

                        buildCommands = param.asString(params.buildCommands, "buildCommands")
                                .get()

                        userColonPassword = pscript.getUserColonPassword("uc_repo_user")
                        clusterRepo = new ClusterRepo(pscript, clusterRepoUrl, userColonPassword)
                        ccliRepo = new CcliRepo(pscript, userColonPassword)
                    }
                }
            }

            stage('Clone repos') {
                steps {
                    script {
                        clusterRepo.pullOrClone()
                        ccliRepo.pullOrClone()
                    }
                }
            }


            stage('Build') {
                steps {
                    script {

                        for (String line : buildCommands.trim().split("\n")) {
                            // todo add enableDebug on demand
                            CcliCommand command = new CcliCommand(clusterRepo.repositoryName)
                            command.build().add(line)
                            ccliRepo.execute(command)
                        }

                    }
                }
            }


        }

    }
}
