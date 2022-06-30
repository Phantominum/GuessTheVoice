package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track


class TrackDAO(var ctx: Context) {
    private var database: FirebaseFirestore
    private var trackRef: CollectionReference

    init {
        database = Firebase.firestore
        trackRef = database.collection("Songs")
    }

    suspend fun getTracks(genre: String): ArrayList<Track>? {
        try {
            var results = trackRef.whereEqualTo("Genre", genre).get().await()
            val tempList = ArrayList<Track>()
            for (doc in results.documents) {
                val song = doc.toObject(Track::class.java)
                if (song != null) {
                    tempList.add(song)
                }
            }
            return tempList
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getTracksID(genre: String): ArrayList<String>? {
        try {
            var results = trackRef.whereEqualTo("Genre", genre).get().await()
            val tempList = ArrayList<String>()
            for (doc in results.documents) {
                val songID = doc.id
                tempList.add(songID)
            }
            return tempList
        } catch (e: Exception) {
            return null
        }
    }

}

