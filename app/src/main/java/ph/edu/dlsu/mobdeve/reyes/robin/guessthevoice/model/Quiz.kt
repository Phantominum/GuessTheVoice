package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class Quiz(
    var quiz_name: String,
    var quiz_creator:String,
    var tracks: ArrayList<Track>,
    var duration: Int=10,
    var genre: String="",
    var created_at: String,
    var description:String="",
    var quiz_image: Int = 1,
    var likes:Int = 0,
) {
    constructor(): this("","", ArrayList(),0,"","","",0,0,)
}