package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.helpers

import java.util.regex.Matcher
import java.util.regex.Pattern

class StringHelper {
    fun validUsername(username: String): Boolean {
        var flag = true
        val p: Pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE)
        val m: Matcher = p.matcher(username)
        val special: Boolean = m.find()
        if (special) {
            println("LOG: Username contains invalid characters")
            return false
        }
        println("LOG: Username is in valid format")
        return true
    }
}