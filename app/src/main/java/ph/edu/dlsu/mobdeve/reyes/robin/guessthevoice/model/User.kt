package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class User(
    var email: String = "",
    var password: String = "",
    var quizzes: ArrayList<String>? = null,
    var liked: ArrayList<String>? = null,
    var username: String = "",
    var top_genres: ArrayList<String> = arrayListOf("Jazz","Classical")
) {
    constructor(): this("","",null,null, "", ArrayList())
}