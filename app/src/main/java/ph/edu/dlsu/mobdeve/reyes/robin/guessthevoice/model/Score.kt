package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class Score (
    var points:Int = 0,
    var quizID: String= "",
    var username:String = "",
    var rank:Int = 0

){
    constructor(): this(-1, "","", -1)
}

data class ScoreView (
    var score : Score? = null,
    var username: String = "",

){
    constructor(): this(null, "")
}