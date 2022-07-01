package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class Score (
    var points:Int = 0,
    var quizId: String= "",
    var username:String = "",
    var rank:Int = 0

){
    constructor(): this(-1, "","", -1)
}