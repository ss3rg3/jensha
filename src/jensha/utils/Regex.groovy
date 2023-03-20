package jensha.utils


import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Wrapper because `Matcher` causes CPS problems.
 */
class Regex {
    private Regex() {}

    /**
     * Returns the first match or null.
     */
    static String getByGroupName(Pattern pattern, String text, String groupName) {
        Matcher matcher = pattern.matcher(text)
        if (!matcher.find()) {
            return null
        }
        return matcher.group(groupName)
    }

    /**
     * Returns the first match or null.
     */
    static String getByGroupIndex(Pattern pattern, String text, int index) {
        Matcher matcher = pattern.matcher(text)
        if (!matcher.find()) {
            return null
        }
        return matcher.group(index)
    }
}


