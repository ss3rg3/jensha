package jensha.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CcliCommandTest {

    @Test
    void terraform() {

        CcliCommand command = new CcliCommand("ccli_uc");
        String result = command.build()
                .enableDebug()
                .add("--match \"jenkins\"")
                .add("--subpath jenkins")
                .add("--push")
                .get();
        assertEquals("python3 ccli.py --debug ccli_uc build --match \"jenkins\" --subpath jenkins --push", result);

    }
}
