package jensha.params;

import jensha._testutils.PScriptFake;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// todo enum test missing
class ParamTest {

    private static final Param param = new Param(new PScriptFake());

    @Test
    public void toBoolean() {
        // Valid
        assertEquals(true, param.asBoolean("true", "checkBox").get());
        assertEquals(true, param.asBoolean(1, "checkBox").get());
        assertEquals(true, param.asBoolean("yes", "checkBox").get());

        assertEquals(false, param.asBoolean("false", "checkBox").get());
        assertEquals(false, param.asBoolean(0, "checkBox").get());
        assertEquals(false, param.asBoolean("no", "checkBox").get());

        // Conditions not met
        assertThrows(IllegalStateException.class, () -> param.asBoolean("true", "checkBox").mustBeFalse().get());
        assertThrows(IllegalStateException.class, () -> param.asBoolean("false", "checkBox").mustBeTrue().get());

        // Invalid
        assertThrows(IllegalStateException.class, () -> param.asBoolean(new StringBuilder(), "checkBox").get());
        assertThrows(IllegalStateException.class, () -> param.asBoolean("", "checkBox").get());
        assertThrows(IllegalStateException.class, () -> param.asBoolean(null, "checkBox").get());
    }

    @Test
    public void toInteger() {
        // Valid
        assertEquals(100, param.asInteger("100", "someNumber").get());

        // Conditions not met
        assertThrows(IllegalStateException.class, () -> param.asInteger("0", "someNumber").isNot(0).get());
        assertThrows(IllegalStateException.class, () -> param.asInteger("10", "someNumber").isLessThan(10).get());
        assertThrows(IllegalStateException.class, () -> param.asInteger("10", "someNumber").isGreaterThan(10).get());

        // Invalid
        assertThrows(IllegalStateException.class, () -> param.asInteger("100.11", "someNumber").get());
        assertThrows(IllegalStateException.class, () -> param.asInteger("asdf", "someNumber").get());
        assertThrows(IllegalStateException.class, () -> param.asInteger(null, "someNumber").get());
    }

    @Test
    public void toStringTest() {
        // Valid
        assertEquals("asdf", param.asString("asdf", "someString").get());

        // Conditions not met
        assertThrows(IllegalStateException.class, () -> param.asString(null, "someString").isNotEmpty().get());
        assertThrows(IllegalStateException.class, () -> param.asString("", "someString").isNotEmpty().get());
        assertThrows(IllegalStateException.class, () -> param.asString("blue", "someString").isOneOf(List.of("red", "green")).get());


        // No test for 'invalid' because even NULL converts to "null"
    }

}
