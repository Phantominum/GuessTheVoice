package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class User(
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var quizzes: ArrayList<Quiz>? = null,
    var liked: ArrayList<Quiz>? = null
) {
    constructor(): this("","","",null,null)
}