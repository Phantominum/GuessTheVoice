package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

data class Genre (
    var genre_color : String = "",
    var genre_name : String =""

) {
    constructor(): this("","")
}