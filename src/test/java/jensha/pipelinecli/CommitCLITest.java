package jensha.pipelinecli;

import jensha._testutils.PScriptFake;
import jensha.utils.PScript;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommitCLITest {

    private static final PScript pscript = new PScriptFake();

    private static final String ANSIBLE = "ansible";
    private static final String K8S = "k8s";
    private static final String NOT_USED = "notused";
    private static final List<String> matchParam = List.of("--match", "-m");
    private static final List<String> subPathParam = List.of("--subpath", "-p");
    private static final List<String> playbooksParam = List.of("--playbooks", "-p");
    private static final List<String> usePublicIpFlag = List.of("--use-public-ip");
    private static final List<String> skipFlag = List.of("--skip", "-s");
    private static final CommitCLI CLI = new CommitCLI()
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
            );
    static {
        CLI.init(new PScriptFake());
    }


    @Test
    void parse() {
        Commit commit = CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansible --match 'k8s-master.*'\n" +
                "/k8s --skip --match 'k8s-master' -p some/path");
        System.out.println(commit);

        assertTrue(CLI.hasCommand(ANSIBLE));
        assertTrue(CLI.hasCommand(K8S));
        assertTrue(CLI.hasCommand(NOT_USED));

        assertEquals(ANSIBLE, CLI.getCommand(ANSIBLE).name);
        assertEquals(K8S, CLI.getCommand(K8S).name);
        assertThrows(IllegalStateException.class, () -> CLI.getCommand("NOT_EXISTING_COMMAND"));

        assertTrue(commit.hasCommand(ANSIBLE));
        assertTrue(commit.hasCommand(K8S));
        assertFalse(commit.hasCommand(NOT_USED));

        Command ansibleCmd = commit.getCommand(ANSIBLE);
        assertTrue(ansibleCmd.hasParameter("--match"));
        assertTrue(ansibleCmd.hasParameter("-m"));
        assertTrue(ansibleCmd.hasParameter(matchParam));
        Parameter requiredParameter = ansibleCmd.requireParameter(matchParam);
        assertEquals("k8s-master.*", requiredParameter.getValue());

        Command k8sCmd = commit.getCommand(K8S);
        assertTrue(k8sCmd.hasParameter(skipFlag));
        assertTrue(k8sCmd.requireFlag(skipFlag).getValue());
    }

    @Test
    void parseUnknownCommand() {
        assertThrows(IllegalStateException.class, () -> CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansib --match 'k8s-master.*'\n" +
                "/k8s --match 'k8s-master'"));
    }

    @Test
    void parseParameterFailures() {
        assertThrows(IllegalStateException.class, () -> CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansible -x k8s/nodes/init_node\n"));

        assertThrows(IllegalStateException.class, () -> CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansible -p -k8s/nodes/init_node\n"));

        assertThrows(IllegalStateException.class, () -> CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansible --match '-k8s-master.*' \n"));

        assertThrows(IllegalStateException.class, () -> CLI.parse("" +
                "added some feature\n" +
                "\n" +
                "/ansible --match -'k8s master' \n"));
    }
}
