package jensha.utils;

import jensha._testutils.PScriptFake;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertThatTest {

    public static final PScript pscript = new PScriptFake();
    public static final AssertThat assertThat = new AssertThat(pscript);

    @Test
    void isInEnum() {
        assertThat.isInEnum("red", Colors.class, "'color' must be one of " + List.of(Colors.values()));

        assertThrows(IllegalStateException.class,
                () -> assertThat.isInEnum("blue", Colors.class, "'color' must be one of " + List.of(Colors.values())));
    }

    @Test
    void isNotEmpty() {

        assertThat.isNotEmpty("red", "'color' must be empty");

        assertThrows(IllegalStateException.class, () -> assertThat.isNotEmpty("", "'color' must be empty"));
        assertThrows(IllegalStateException.class, () -> assertThat.isNotEmpty(null, "'color' must be empty"));
    }

    @Test
    void isNotNull() {
        assertThat.isNotNull("red", "'color' must be null");
        assertThat.isNotNull("", "'color' must be null");

        assertThrows(IllegalStateException.class, () -> assertThat.isNotNull(null, "'color' must not be null"));
    }

    @Test
    void isTrue() {
        assertThat.isTrue(true, "'areYouSure' must be true");
        assertThrows(IllegalStateException.class, () -> assertThat.isTrue(false, "'areYouSure' must be true"));
    }

    @Test
    void isOneOf() {
        List<String> colors = List.of("red", "green", "blue");
        assertThat.isOneOf("red", colors, "'color' must be one of " + colors);

        assertThrows(IllegalStateException.class, () -> assertThat.isOneOf("yellow", colors, "'color' must be one of " + colors));
    }

    private enum Colors {
        red,
        green,
        yellow
    }
}
