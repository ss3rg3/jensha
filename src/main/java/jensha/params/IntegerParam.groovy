package jensha.params

import jensha.utils.AssertThat
import jensha.utils.PScript

class IntegerParam implements Serializable {

    private final PScript pscript
    private final Integer param
    private final String paramName
    private final AssertThat assertThat

    IntegerParam(PScript pscript, Integer param, String paramName) {
        this.pscript = pscript
        this.paramName = paramName
        this.assertThat = new AssertThat(pscript)
        this.param = param
    }

    /**
     * Fails if value is the same as provided
     */
    IntegerParam isNot(Integer value) {
        if (this.param == value) {
            this.pscript.exitWithError("'${paramName}' must not be 0, got '${param}'")
        }
        return this
    }

    /**
     * Fails if value is the less than value provided
     */
    IntegerParam isGreaterThan(Integer value) {
        if (this.param <= value) {
            this.pscript.exitWithError("'${paramName}' must be greater than ${value}, got '${param}'")
        }
        return this
    }

    /**
     * Fails if value is the greater than value provided
     */
    IntegerParam isLessThan(Integer value) {
        if (this.param >= value) {
            this.pscript.exitWithError("'${paramName}' must be less than ${value}, got '${param}'")
        }
        return this
    }

    Integer get() {
        return this.param
    }
}
