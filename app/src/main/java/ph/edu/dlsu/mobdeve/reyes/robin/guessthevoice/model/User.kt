package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class User(
    var email: String = "",
    var password: String = "",
    var quizzes: ArrayList<Int>? = null,
    var liked: ArrayList<Int>? = null
) {
    constructor(): this("","",null,null)
}