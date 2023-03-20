package jensha.params

import jensha.utils.AssertThat
import jensha.utils.PScript

class BooleanParam implements Serializable {

    private final PScript pscript
    private final Boolean param
    private final String paramName
    private final AssertThat assertThat

    BooleanParam(PScript pscript, Boolean param, String paramName) {
        this.pscript = pscript
        this.paramName = paramName
        this.assertThat = new AssertThat(pscript)
        this.param = param
    }

    BooleanParam mustBeTrue() {
        this.assertThat.isTrue(this.param, "'${paramName}' must be true, got '${param}'")
        return this
    }

    BooleanParam mustBeFalse() {
        this.assertThat.isFalse(this.param, "'${paramName}' must be false, got '${param}'")
        return this
    }

    Boolean get() {
        return this.param
    }

}
