package jensha.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SharedLibraryTest {

    @Test
    public void getAnnotation() {
        assertEquals("@Library('uc_jensha') _", SharedLibrary.getAnnotation());
        assertEquals("@Library('uc_jensha@some-branch') _", SharedLibrary.getAnnotation("some-branch"));
        assertEquals("@Library('name-of-lib@some-branch') _", SharedLibrary.getAnnotation("name-of-lib", "some-branch"));
    }

}
