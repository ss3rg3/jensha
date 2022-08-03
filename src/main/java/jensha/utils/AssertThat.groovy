package jensha.utils

class AssertThat {

    private final PScript pscript

    AssertThat(PScript pscript) {
        this.pscript = pscript
    }

    /**
     * Fails if value is "", null or 'null' (string)
     */
    void isNotEmpty(String value, String errorMessage) {
        if (value == "" || value == null) {
            pscript.exitWithError(errorMessage)
        }
        if (value == "null") {
            pscript.exitWithError("Provided value was 'null' (string), errorMessage: ${errorMessage}")
        }
    }

    /**
     * Fails if value is doesn't occur in iterable
     */
    void isOneOf(String value, Iterable<String> iterable, String errorMessage) {
        for (String item : iterable) {
            if (item == value) {
                return
            }
        }
        pscript.exitWithError(errorMessage)
    }

    /**
     * Fails if value is null or 'null' (string)
     */
    void isNotNull(Object value, String errorMessage) {
        if (value == null) {
            pscript.exitWithError(errorMessage)
        }
        if (value == "null") {
            pscript.exitWithError("Provided value was 'null' (string), errorMessage: ${errorMessage}")
        }
    }

    void isTrue(boolean value, String errorMessage) {
        if (!value) {
            pscript.exitWithError(errorMessage)
        }
    }

    void isFalse(boolean value, String errorMessage) {
        if (value) {
            pscript.exitWithError(errorMessage)
        }
    }

    void isInEnum(String value, Class enumClass, String errorMessage) {
        try {
            Enum.valueOf(enumClass, value)
        } catch(Exception e) {
            pscript.exitWithError(errorMessage, e)
        }
    }


}
