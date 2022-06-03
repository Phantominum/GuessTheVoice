package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLogOut.setOnClickListener{
            var goToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(goToMainActivity)
        }

        binding.buttonSettingsBack.setOnClickListener(){
            var goToDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(goToDashboard)
        }
    }
}