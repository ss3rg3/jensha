package jensha.params

import jensha.utils.PScript

class Param implements Serializable {

    private final PScript pscript

    Param(PScript pscript) {
        this.pscript = pscript
    }

    StringParam asString(Object param, String paramName) {
        try {
            String value = String.valueOf(param).trim()
            return new StringParam(this.pscript, value, paramName)
        } catch(Exception e) {
            this.pscript.exitWithError("Failed to cast '${paramName}' to String, got ${param}", e)
        }
        throw new IllegalStateException("Unreachable code")
    }

    IntegerParam asInteger(Object param, String paramName) {
        try {
            Integer value = Integer.valueOf((String) param)
            return new IntegerParam(this.pscript, value, paramName)
        } catch(Exception e) {
            this.pscript.exitWithError("Failed to cast '${paramName}' to Integer, got '${param}'", e)
        }
        throw new IllegalStateException("Unreachable code")
    }

    BooleanParam asBoolean(Object param, String paramName) {
        try {
            String stringParam = ((String) param).trim()
            switch (stringParam) {
                case "true":
                case "1":
                case "yes":
                    return new BooleanParam(this.pscript, true, paramName)
                case "false":
                case "0":
                case "no":
                    return new BooleanParam(this.pscript, false, paramName)
                default:
                    throw new IllegalArgumentException("Param has no value which can interpreted as Boolean, got '${stringParam}'")
            }
        } catch(Exception e) {
            this.pscript.exitWithError("Failed to cast '${paramName}' to Integer, got '${param}'", e)
        }
        throw new IllegalStateException("Unreachable code")
    }

    EnumParam asEnum(Object param, Class<? extends Enum> enumClass, String paramName) {
        try {
            Enum enumParam = Enum.valueOf(enumClass, String.valueOf(param))
            return new EnumParam<? extends Enum>(this.pscript, enumParam, enumClass, paramName)
        } catch (Exception e) {
            this.pscript.exitWithError("Failed to cast '${paramName}' to Enum, got '${param}'", e)
        }
        throw new IllegalStateException("Unreachable code")
    }

}
