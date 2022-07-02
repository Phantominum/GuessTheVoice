package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLandingLogIn.setOnClickListener{

            var goToLogIn = Intent(this, LoginActivity:: class.java)
            startActivity(goToLogIn)

        }

        binding.buttonLandingSignUp.setOnClickListener {
            val goToRegister = Intent(this,RegisterActivity::class.java)
            startActivity(goToRegister)
        }


    }

    override fun onBackPressed() {
        if (intent.extras == null)
            super.onBackPressed()
        else
            println("You must log in first")
    }
}