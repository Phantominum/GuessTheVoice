package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class User(
    var email: String = "",
    var password: String = "",
    var quizzes: ArrayList<Int>? = null,
    var liked: ArrayList<Int>? = null,
    var username: String = ""
) {
    constructor(): this("","",null,null, "")
}