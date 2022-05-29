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

        binding.logIn.setOnClickListener{
            view: View?-> Toast.makeText(applicationContext, "Logged in", Toast.LENGTH_SHORT).show()

            var goToDashboard = Intent(this, DashboardActivity:: class.java)
            startActivity(goToDashboard)

        }



    }
}