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
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLoginBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.helpers.StringHelper
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var textError: TextView
    private lateinit var dao: UserDAO
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set dao
        dao = UserDAO(applicationContext)
        // Set fields
        etUsername = binding.loginUsername
        etPassword = binding.loginPassword
        textError = binding.loginError
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            textError.text = ""
            authenticate()
        }


    }
    private fun authenticate() {
        val username = etUsername.text.toString()
        val email = username + "@guessthevoice.com"
        val password = etPassword.text.toString()
        // Check if username in valid format
        val validUsername = StringHelper().validUsername(username)
        // Authenticate if fields aren't empty and username is valid
        if (email.isNotEmpty() && password.isNotEmpty() && validUsername) {
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        // Fetch the user from the DB
                        var user = dao.getAccount(username)
                        // Erase fields
                        clearFields()
                        // Pass the data to next activity
                        val bundle = Bundle()
                        bundle.putString("username", username)
                        val goToDashboard = Intent(this,DashboardActivity::class.java)
                        goToDashboard.putExtras(bundle)
                        startActivity(goToDashboard)
                    }
                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_LONG).show()
                    println("LOG: Failed to log in ${err}")
                    textError.text = err.message
                    textError.setTextColor(getResources().getColor(R.color.vibrant_pink))
                }
        } else {
            Toast.makeText(this,"Fill all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        etUsername.text.clear()
        etPassword.text.clear()
        textError.text = ""
        textError.setTextColor(getResources().getColor(R.color.white))
    }

}