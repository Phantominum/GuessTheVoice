package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class UserDAO(var ctx: Context) {
    private val DB_URL:String = "https://guessthevoice-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private var database: FirebaseDatabase
    private var dbRef: DatabaseReference

    init {
        database = Firebase.database(DB_URL)
        dbRef = database.getReference("Accounts")
    }

    fun createAccount(user: User): Int {
        var success = 0
        dbRef.child(user.username).setValue(user).addOnSuccessListener {
            Toast.makeText(ctx, "Successfully registered!",Toast.LENGTH_SHORT).show()
            success = 1
        }.addOnFailureListener {
            Toast.makeText(ctx, "Unable to register account", Toast.LENGTH_SHORT).show()
        }
        return success
    }

    fun getAccount(username: String): User? {
        var user : User? = null
        dbRef.child(username).get().addOnSuccessListener {
            if (it.exists()) {
                user = it.getValue(User::class.java)
                if (user != null)
                    println("LOG: Received ${user!!.username} data")
            } else {
                Toast.makeText(ctx, "Account doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }
        return user
    }

}