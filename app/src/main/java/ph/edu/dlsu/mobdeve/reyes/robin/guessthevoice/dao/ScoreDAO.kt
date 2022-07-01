package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.Context
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score
import java.lang.Exception

class ScoreDAO(var ctx: Context){
    private var database: FirebaseFirestore
    private var dbCollection : CollectionReference
    init {
        database = Firebase.firestore
        dbCollection = database.collection("Scores")
    }

    suspend fun getScores(quizId: String): ArrayList<Score>? {
        try {
            val results = dbCollection.whereEqualTo("quizID", quizId).get().await()
            println("FINISHED GETTING RESULTS ${results.documents.size}")
            val tempList = ArrayList<Score>()
            for (doc in results.documents) {
                val score = doc.toObject(Score::class.java)
                if (score != null) {
                    tempList.add(score)
                }
            }
            println(tempList.size)
            tempList.sortByDescending {
                it.points
            }
            for(score in tempList){
                score.rank = tempList.indexOf(score)+1
            }
            return tempList
        }catch(e: Exception){
            println("ERROR: No scores found!")
            return null
        }
    }


}

