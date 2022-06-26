package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
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
        var user = null
        try {
            val res = dbCollection.whereEqualTo("email",email).get().await()
            return res.documents[0].toObject(User::class.java)
        } catch(e: Exception) {
            return null
        }
    }

//    fun getAccountDocID(email: String): String? {
//        var userID : String? = null
//        dbCollection.whereEqualTo("email",email).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                userID =  it.result.documents[0].id
//                if (userID != null)
//                    println("LOG: Received userID ${userID} data")
//                else
//                    println("LOG: No userID for ${email} was found")
//            } else {
//                println("LOG: No Snapshot received in getAccountDocID")
//            }
//        }
//        return userID
//    }

//
//    suspend fun addQuizToUser(id: Int, email: String) {
//        // Get user docID
//        var userID = getAccountDocID("email")
//
//         Update quizzes field of user
//        if (userID != null) {
//            dbCollection.document(userID).update("quizzes",quizzes)
//                .addOnSuccessListener {
//                    Toast.makeText(ctx, "Quiz has been created", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

}