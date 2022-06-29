package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityLoginBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.helpers.StringHelper
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.User

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var etEmail: EditText
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
        etEmail = binding.loginEmail
        etPassword = binding.loginPassword
        textError = binding.loginError
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            textError.text = ""
            // Get fields
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            // Authenticate if fields aren't empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    // Async assign auth res
                    val authRes = async {
                        authenticate(email, password)
                    }
                    if (authRes.await() != null)
                        Log.d(TAG, "AUTH RES is ${authRes.await()!!.user}")
                    // Async get user data after authentication
                    val user = async {
                        dao.getAccount(email)
                    }
                    if (user.await() != null)
                        Log.d(TAG, "USER is ${user.await()!!.email}")
                    // Switch to main thread to make UI changes
                    withContext(Dispatchers.Main) {
                        if (authRes.await() != null) {
                            if (authRes.await()!!.user != null) {
                                val user_email = user.await()?.email
                                println("Account is found ${user_email}")
                                val bundle = Bundle()
                                bundle.putString("email", user_email)
                                val goToDashboard =
                                    Intent(this@LoginActivity, DashboardActivity::class.java)
                                goToDashboard.putExtras(bundle)
                                startActivity(goToDashboard)
                                finish()
                            } else {
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Account not found",
//                                    Toast.LENGTH_LONG
//                                ).show()
                                Snackbar.make(binding.root,
                                    "Account not found.",
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }

        }
    }

    suspend fun authenticate(email: String, password: String) : AuthResult? {
        try {
            val res = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return res
        } catch (err: Exception) {
            Handler(Looper.getMainLooper()).post {
//                Toast.makeText(this, "Failed to log in.", Toast.LENGTH_LONG).show()
                Snackbar.make(binding.root, "Failed to Log in", Snackbar.LENGTH_LONG).show()
                println("LOG: Failed to log in ${err}")
                textError.text = err.message
                clearFields()
            }
            return null
        }

    }

    private fun clearFields() {
        etEmail.text.clear()
        etPassword.text.clear()
        textError.setTextColor(getResources().getColor(R.color.white))
    }

}