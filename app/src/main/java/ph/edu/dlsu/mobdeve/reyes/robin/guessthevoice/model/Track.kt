package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track (
    var name: String? = null,
    var artist: String? = null,
    var music_url: String? = null
): Parcelable