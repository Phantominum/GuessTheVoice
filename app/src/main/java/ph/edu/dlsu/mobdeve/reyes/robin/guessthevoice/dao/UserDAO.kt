package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class UserDAO(var ctx: Context) {
    private var database: FirebaseFirestore
    private var dbCollection : CollectionReference
    init {
        database = Firebase.firestore
        dbCollection = database.collection("Accounts")
    }

    fun createAccount(user: User): String? {
        var docId: String? = null
        dbCollection.add(user).addOnSuccessListener {
            Toast.makeText(ctx, "Successfully registered!",Toast.LENGTH_SHORT).show()
            docId = it.id
        }.addOnFailureListener {
            Toast.makeText(ctx, "Unable to register account", Toast.LENGTH_SHORT).show()
        }
        return docId
    }

    suspend fun getAccount(email: String) : User? {
        try {
            val res = dbCollection.whereEqualTo("email",email).get().await()
            return res.documents[0].toObject(User::class.java)
        } catch(e: Exception) {
            return null
        }
    }

    suspend fun getAccountDocID(email: String): String? {
        try {
            val res = dbCollection.whereEqualTo("email",email).get().await()
            return res.documents[0].id
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun addQuizToUser(userID: String, quizID: String): Boolean {
        try {
            dbCollection.document(userID)
                .update("quizzes",(FieldValue.arrayUnion(quizID)))
                .await()
            return true
        } catch(e:Exception) {
            println("LOG: Unable to add quiz to user")
            return false
        }
    }

}