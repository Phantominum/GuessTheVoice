package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityRegisterBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var dbRef: DatabaseReference
//    private val DB_URL:String = "https://guessthevoice-default-rtdb.asia-southeast1.firebasedatabase.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etEmail = binding.registerEmail
        etPassword = binding.registerPassword
        btnRegister = binding.btnRegister

        firebaseAuth = FirebaseAuth.getInstance()
//        database = Firebase.database(DB_URL)
//        dbRef = database.getReference("Accounts")

        btnRegister.setOnClickListener {
            println("LOG: Register button clicked")
            saveAccountData()
        }

        binding.btnLogin.setOnClickListener {
            val goToLogin = Intent(this,LoginActivity::class.java)
            startActivity(goToLogin)
        }
    }
    private fun saveAccountData() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                Toast.makeText(this, "Account created.", Toast.LENGTH_LONG).show()
                etEmail.text.clear()
                etPassword.text.clear()
                println("LOG: Account created")
            }.addOnFailureListener { err ->
                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_LONG).show()
                println("LOG: Failed account creation: ${err}")
            }
        } else {
            Toast.makeText(this,"Fill all fields", Toast.LENGTH_LONG).show()
        }
//        val uuid = dbRef.push().key!! // Create a Unique ID for the user
//        val user = User(username, password)
//
//        dbRef.child(uuid).setValue(user)
//            .addOnCompleteListener {
//                Toast.makeText(this, "Account created.", Toast.LENGTH_LONG).show()
//                etUsername.text.clear()
//                etPassword.text.clear()
//                println("LOG: Account created")
//            }.addOnFailureListener { err ->
//                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_LONG).show()
//                println("LOG: Failed account creation")
//            }
    }
}