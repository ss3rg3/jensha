package jensha.utils

class Types implements Serializable {

    public static Integer convertToInteger(PScript pscript, Object param, String paramName) {
        try {
            return Integer.valueOf((String) param)
        } catch(Exception e) {
            pscript.exitWithError("Failed to cast '${paramName}' to Integer", e)
            throw new IllegalStateException("Program will exit beforehand")
        }
    }

}
