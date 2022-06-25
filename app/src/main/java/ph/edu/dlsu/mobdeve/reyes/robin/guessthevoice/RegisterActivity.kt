package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityRegisterBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.helpers.StringHelper
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.*

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var textError: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dao : UserDAO

    private var errors : ArrayList<Errors> = ArrayList()

    private var stringhelper = StringHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setup dao
        dao = UserDAO(applicationContext)
        // Set fields
        etUsername = binding.registerUsername
        etPassword = binding.registerPassword
        btnRegister = binding.btnRegister
        textError = binding.registerError
        // Setup auth
        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            println("LOG: Register button clicked")
            // Clear existing errors
            errors.clear()
            showErrorMessages()
            saveAccountData()
        }

        etUsername.addTextChangedListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            // Check if username in valid format
            val validUsername = stringhelper.validUsername(username)
            if (!validUsername) {
                if (errors.size < 1) {
                    errors.add(InvalidUsername())
                }
                showErrorMessages()
            } else {
                // Remove the error if valid
                for (e in errors) {
                    if (e is InvalidUsername)
                        errors.remove(e)
                }
                showErrorMessages()
            }
        }

    }
    private fun saveAccountData() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        // Check if username in valid format
        val validUsername = stringhelper.validUsername(username)
        // Create if fields aren't empty
        if (username.isNotEmpty() && password.isNotEmpty() && validUsername) {
            var user_email = username + "@guessthevoice.com"
            firebaseAuth.createUserWithEmailAndPassword(user_email,password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Account created.", Toast.LENGTH_LONG).show()
                        etUsername.text.clear()
                        etPassword.text.clear()
                        textError.text = ""
                        textError.run { setTextColor(resources.getColor(R.color.white)) }
                        println("LOG: Auth credentials created")
                        var user = User(username, password, user_email)
                        var res = dao.createAccount(user)
                        if (res == 1)
                            println("LOG: Received successful registration")
                        else
                            println("LOG: Received failed registration")
                    }
            }.addOnFailureListener { err ->
                    Toast.makeText(this, "Failed to create account.", Toast.LENGTH_LONG).show()
                    println("LOG: Failed account creation: $err")
                    if (!err.message.isNullOrEmpty())
                        errors.add(FirebaseError(err.message!!))
                    showErrorMessages()
            }
        } else {
            // Add error messages depending on the case
            if (username.isEmpty() || password.isEmpty())
                errors.add(InputError("Some field/s are empty."))
            if (!validUsername)
                errors.add(InvalidUsername())
            showErrorMessages()
        }
    }

    fun showErrorMessages() {
        // Arraylist of strings
        var errorString : ArrayList<String> = ArrayList()
        for (e in errors)
            errorString.add(e.getErrorMessage())
        // Concatenate errors to one string
        textError.text = errorString.joinToString("\n")
        textError.setTextColor(resources.getColor(R.color.vibrant_pink))
    }
}