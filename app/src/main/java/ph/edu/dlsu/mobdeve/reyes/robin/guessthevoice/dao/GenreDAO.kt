package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre
import java.lang.Exception

class GenreDAO(var ctx : Context) {
    private var database: FirebaseFirestore
    private var dbCollection : CollectionReference
    init {
        database = Firebase.firestore
        dbCollection = database.collection("Genres")
    }

    suspend fun getGenres(): ArrayList<Genre>? {
        try {
            var results = dbCollection.get().await()
            var tempList = ArrayList<Genre>()
            for (doc in results.documents) {
                var genre = doc.toObject(Genre::class.java)
                if (genre != null) {
                    tempList.add(genre)
                }
            }
            return tempList
        }catch(e: Exception){
            return null
        }


    }



}