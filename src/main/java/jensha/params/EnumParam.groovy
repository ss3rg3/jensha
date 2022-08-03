package jensha.params

import jensha.utils.AssertThat
import jensha.utils.PScript

class EnumParam<T> implements Serializable {

    private final PScript pscript
    private final T param
    private final String paramName
    private final AssertThat assertThat

    EnumParam(PScript pscript, Enum param, Class<T> enumClass, String paramName) {
        this.pscript = pscript
        this.paramName = paramName
        this.assertThat = new AssertThat(pscript)
        this.param = (T) param
    }

    T get() {
        return this.param
    }
}
