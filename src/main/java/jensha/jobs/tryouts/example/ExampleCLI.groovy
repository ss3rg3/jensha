package jensha.jobs.tryouts.example

import jensha.pipelinecli.Command
import jensha.pipelinecli.CommitCLI
import jensha.utils.PScript

class ExampleCLI {

    public static final String ANSIBLE = "ansible"
    public static final String K8S = "k8s"
    public static final String NOT_USED = "notused"
    public static final List<String> matchParam = List.of("--match", "-m")
    public static final List<String> subPathParam = List.of("--subpath", "-p")
    public static final List<String> playbooksParam = List.of("--playbooks", "-p")
    public static final List<String> usePublicIpFlag = List.of("--use-public-ip")
    public static final List<String> skipFlag = List.of("--skip", "-s")

    static CommitCLI create(PScript pscript) {
        return new CommitCLI(pscript)
                .help("Pipeline commands which resemble the command 'ansible' & 'k8s' of CCLI")
                .addCommand(new Command(ANSIBLE)
                        .addFlag(usePublicIpFlag, "Will use the public IP of the instance")
                        .addParam(matchParam, "For --match parameter in CCLI")
                        .addParam(playbooksParam, "For --playbooks parameter in CCLI")
                )
                .addCommand(new Command(K8S)
                        .addFlag(skipFlag, "Will skip the Kubernetes stage")
                        .addParam(matchParam, "For --match parameter in CCLI")
                        .addParam(subPathParam, "For --subpath parameter in CCLI")
                )
                .addCommand(new Command(NOT_USED)
                        .addFlag(skipFlag, "Not used")
                )
    }

    static String getHelpText() {
        return create(null).toString()
    }

}
