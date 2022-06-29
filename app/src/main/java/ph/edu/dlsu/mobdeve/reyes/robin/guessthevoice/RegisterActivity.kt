package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var etEmail: EditText
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
        etEmail = binding.registerEmail
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
    }
    private fun saveAccountData() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        // Create if fields aren't empty
        if (email.isNotEmpty() && password.isNotEmpty()) {

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if (it.isSuccessful) {
//                        Toast.makeText(this, "Account created.", Toast.LENGTH_LONG).show()
                        Snackbar.make(binding.root, "Account created.", Snackbar.LENGTH_LONG).show()
                        etEmail.text.clear()
                        etPassword.text.clear()
                        textError.text = ""
                        textError.run { setTextColor(resources.getColor(R.color.white)) }
                        println("LOG: Auth credentials created")
                        val user = User(email, password)
                        val res = dao.createAccount(user)
                        if (res != null)
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
            if (email.isEmpty())
                errors.add(InputError("Email field is empty."))
            if (password.isEmpty())
                errors.add(InputError("Email field is empty."))
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