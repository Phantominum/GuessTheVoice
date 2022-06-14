package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class Quiz (
    var quiz_name: String = "",
    var quiz_creator:String = "",
    var likes:Int = 0,
    var quiz_image: Int = 1,
    var duration: Int=10,
    var genre: String=""
)