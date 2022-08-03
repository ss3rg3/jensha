package jensha.params

import jensha.utils.AssertThat
import jensha.utils.PScript

class StringParam implements Serializable {

    private final PScript pscript
    private final String param
    private final String paramName
    private final AssertThat assertThat

    StringParam(PScript pscript, String param, String paramName) {
        this.pscript = pscript
        this.paramName = paramName
        this.assertThat = new AssertThat(pscript)
        this.param = param
    }

    /**
     * Fails if value is "", null or 'null' (string)
     */
    StringParam isNotEmpty() {
        this.assertThat.isNotEmpty(this.param, "'${paramName}' must not be empty, got '${param}'")
        return this
    }

    /**
     * Fails if value is doesn't occur in iterable
     */
    StringParam isOneOf(Iterable<String> iterable) {
        this.assertThat.isOneOf(this.param, iterable, "'${paramName}' must be one of ${iterable}, got '${this.param}'")
        return this
    }

    /**
     * If you don't use isNotEmpty() and the value is empty (""), then NULL is returned.
     * Same for the string "null"
     */
    String get() {
        if(this.param == "" || this.param == "null") {
            return null
        }
        return this.param
    }
}
