package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
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
            val results = dbCollection.get().await()
            val tempList = ArrayList<Genre>()
            for (doc in results.documents) {
                val genre = doc.toObject(Genre::class.java)
                if (genre != null) {
                    tempList.add(genre)
                }
            }
            return tempList
        }catch(e: Exception){
            return null
        }
    }

    suspend fun getGenreID(genre: String): String? {
        try {
            val res = dbCollection.whereEqualTo("genre_name",genre).get().await()
            return res.documents[0].id
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun addQuizToGenre(genreID: String, quizID: String): Boolean {
        try {
            dbCollection.document(genreID)
                .update("quizzes",(FieldValue.arrayUnion(quizID)))
                .await()
            return true
        } catch(e:Exception) {
            println("LOG: Unable to add quiz to genre")
            return false
        }
    }

    suspend fun getQuizzes(genre: String): ArrayList<String>? {
        try {
            val query = dbCollection.whereEqualTo("genre_name", genre).get().await()
            var res = ArrayList<String>()
            val genre = query.documents[0].toObject(Genre::class.java)
            if (genre != null)
                return genre.quizzes
            else
                return null
        } catch (e: Exception) {
            return null
        }
    }

}