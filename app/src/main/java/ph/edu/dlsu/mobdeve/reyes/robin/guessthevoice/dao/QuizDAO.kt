package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao


import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class QuizDAO(var ctx: Context) {
    private var database: FirebaseFirestore
    private var quizRef: CollectionReference
    private var userRef: CollectionReference

    init {
        database = Firebase.firestore
        quizRef = database.collection("Quizzes")
        userRef = database.collection("Accounts")
    }

    fun createQuiz(quiz: Quiz, email: String): String? {
        var id: String? = null
        var doc = quizRef.add(quiz).addOnSuccessListener {
            Toast.makeText(ctx, "Successfully added a quiz!", Toast.LENGTH_SHORT).show()
            id = it.id
        }.addOnFailureListener {
            Toast.makeText(ctx, "Unable to add a quiz!", Toast.LENGTH_SHORT).show()
        }
        return id
    }
}