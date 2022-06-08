package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track

interface TrackDAO {
    fun addTrack(track: Track)
    fun getTracks(): ArrayList<Track>
    fun removeTrack(position: Int)
}

class TrackDAOArrayImpl: TrackDAO {
    private val tracks = ArrayList<Track>()
    override fun addTrack(track: Track) {
        tracks.add(track)
    }

    override fun getTracks(): ArrayList<Track> {
       return tracks
    }

    override fun removeTrack(position: Int) {
        tracks.removeAt(position)
    }
}

