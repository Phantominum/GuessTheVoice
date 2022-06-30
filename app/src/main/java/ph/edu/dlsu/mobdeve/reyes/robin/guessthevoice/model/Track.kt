package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track (
    var SongName: String? = null,
    var Artist: String? = null,
    var Audio_URL: String? = null,
    var Genre: String = ""
): Parcelable {
    constructor() : this("","", "","")
}