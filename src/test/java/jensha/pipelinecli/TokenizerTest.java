package jensha.pipelinecli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void singleLine() {
        List<String> tokens = Tokenizer.tokenize("ansible --match \"k8s-master.*\" --playbooks 'k8s/nodes/init_node' --some-option asdf1234 --use-public-ip");
        assertEquals(8 , tokens.size());
    }

    @Test
    void specialChars() {
        List<String> tokens = Tokenizer.tokenize("ansible --playbooks 'k8s\nnodes\ninit_node'", true);
        System.out.println(tokens);
    }

}
