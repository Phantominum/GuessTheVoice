package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao


import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class QuizDAO(var ctx: Context) {
    private var database: FirebaseFirestore
    private var quizRef: CollectionReference

    init {
        database = Firebase.firestore
        quizRef = database.collection("Quizzes")
    }

    suspend fun createQuiz(quiz: Quiz): String? {
        try {
            var doc = quizRef.add(quiz).await()
            return doc.id
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getQuizById(id: String): Quiz? {
        try {
            val doc = quizRef.whereEqualTo(FieldPath.documentId(), id).get().await()
            return doc.documents[0].toObject(Quiz::class.java)
        } catch (e: Exception) {
            return null
        }
    }
}