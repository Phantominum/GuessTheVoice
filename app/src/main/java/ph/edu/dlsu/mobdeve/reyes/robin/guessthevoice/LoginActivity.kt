package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLoginBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var textError: TextView
    //    private val DB_URL:String = "https://guessthevoice-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        etEmail = binding.loginEmail
        etPassword = binding.loginPassword
        textError = binding.loginError
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            textError.text = ""
            authenticate()
        }


    }
    private fun authenticate() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        etEmail.text.clear()
                        etPassword.text.clear()
                        textError.text = ""
                        textError.setTextColor(getResources().getColor(R.color.white))
//                        view: View?-> Toast.makeText(applicationContext, "Logged in", Toast.LENGTH_SHORT).show()
                        val goToDashboard = Intent(this,DashboardActivity::class.java)
                        startActivity(goToDashboard)
                    }
                }.addOnFailureListener { err ->
//                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_LONG).show()
                    println("LOG: Failed to log in ${err}")
                    textError.text = err.message
                    textError.setTextColor(getResources().getColor(R.color.vibrant_pink))
                }
        } else {
            Toast.makeText(this,"Fill all fields", Toast.LENGTH_LONG).show()
        }
    }
//
//    private fun authenticate(username: String, password: String): Boolean {
//        val database = Firebase.database(DB_URL)
//        var dbRef = database.getReference("Accounts")
//        var query = dbRef.child("username").equalTo(username.trim())
//        var is_found = false
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                if (dataSnapshot.exists()) {
//                    for (userSnap in dataSnapshot.children) {
//                        var user: User? = dataSnapshot.getValue(User::class.java)
//                        if (user != null) {
//                            if  (user.password.equals(password)){
//                               is_found = true
//                            }
//                            println("Name ${user.username}, Pass ${user.password}")
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//            }
//        })
//        return is_found
//    }
}