package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class User(
    var email: String = "",
    var password: String = "",
    var username: String = "",
    var liked: ArrayList<String>? = null,
    var quizzes: ArrayList<String>? = null,
    var top_genres: ArrayList<String> = arrayListOf("Jazz","Classical")
) {
    constructor(): this("","","", ArrayList(), ArrayList(), ArrayList())
}