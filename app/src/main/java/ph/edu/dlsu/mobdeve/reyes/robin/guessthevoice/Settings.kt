package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var username: EditText
    private lateinit var spinner1 : Spinner
    private lateinit var spinner2 : Spinner
    private lateinit var selectedGenre1: String
    private lateinit var selectedGenre2: String
    private lateinit var genreDAO : GenreDAO
    private lateinit var userDAO : UserDAO
    private var genres = ArrayList<String>()

    private lateinit var userID : String
    private lateinit var currUsername : String
    private lateinit var currTopGenre1 : String
    private lateinit var currTopGenre2 : String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize dao
        genreDAO = GenreDAO(applicationContext)
        userDAO = UserDAO(applicationContext)
        // Initialize spinners
        username = binding.etUsername
        spinner1 = binding.topGenre1
        spinner2 = binding.topGenre2
    }

    override fun onResume() {
        super.onResume()
        // Get bundle data
        val bundle = intent.extras
        if (bundle != null) {
            userID = bundle.getString("userID")!!
            currUsername = bundle.getString("username")!!
            currTopGenre1 = bundle.getString("topGenre1")!!
            currTopGenre2 = bundle.getString("topGenre2")!!
        } else {
            println("NO BUNDLE RECEIVED")
            val goToMainActivity = Intent(this, DashboardActivity::class.java)
            startActivity(goToMainActivity)
            finish()
        }
        // Spinner backend
        setupSpinner1()
        setupSpinner2()
        // Setup initial data
        println("LOG: Settings top genres are $currTopGenre1 and $currTopGenre2")
        println("LOG: There are ${genres.size} genres found")
        username.setText(currUsername)

        binding.buttonLogOut.setOnClickListener{
            val mainBundle = Bundle()
            mainBundle.putInt("loggedOut", 1)
            var goToMainActivity = Intent(this, MainActivity::class.java)
            goToMainActivity.putExtras(mainBundle)
            startActivity(goToMainActivity)
        }

        binding.buttonSettingsBack.setOnClickListener(){
            var goToDashboard = Intent(this, DashboardActivity::class.java)
            val bundle = Bundle()
            var dashboard_bundle = intent.extras
            val email = dashboard_bundle!!.getString("email")
            bundle.putString("email", email)
            goToDashboard.putExtras(bundle)
            startActivity(goToDashboard)
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedGenre1 = genres.get(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedGenre2 = genres.get(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }

        binding.btnUpdate.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                // TODO: Strip spaces from username
                var updateUsername : Deferred<Boolean> = async {return@async false}
                var updateGenres: Deferred<Boolean> = async{return@async false}
                if (currUsername != username.text.toString()) {
                    updateUsername = async {userDAO.updateUsername(userID, username.text.toString())}
                }
                if (currTopGenre1 != selectedGenre1 || currTopGenre2 != selectedGenre2) {
                    val newTopGenres = ArrayList<String>()
                    newTopGenres.add(selectedGenre1)
                    newTopGenres.add(selectedGenre2)
                    updateGenres = async { userDAO.updateTopGenres(userID, newTopGenres) }
                }
                withContext(Dispatchers.Main) {
                    if (updateUsername.await() || updateGenres.await()) {
                        Toast.makeText(applicationContext,
                            "Settings have been updated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun setupSpinner1() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Retrieve genre names
            val choices = async { genreDAO.getGenres() }
            for (value in choices.await()!!)
                genres.add(value.genre_name)
            // Update spinner
            withContext(Dispatchers.Main) {
                val tempList = ArrayList<String>()
                // Extract the names
                for (value in choices.await()!!)
                    tempList.add(value.genre_name)
                // Setup the spinner
                val arrayAdapter = this@Settings.let {
                    ArrayAdapter(
                        it,
                        R.layout.spinner_dark_item,
                        tempList.toArray()
                    )
                }
                spinner1.adapter = arrayAdapter
                println("LOG: Spinner 1 genres size is ${genres.size}")
                val initTopGenre1 = genres.indexOf(currTopGenre1)
                println("LOG: $currTopGenre1 with idx $initTopGenre1")
                spinner1.setSelection(initTopGenre1)
            }
        }
    }

    private fun setupSpinner2() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Retrieve genre names
            val choices = async { genreDAO.getGenres() }
            withContext(Dispatchers.Main) {
                val tempList = ArrayList<String>()
                // Extract the names
                for (value in choices.await()!!)
                    tempList.add(value.genre_name)

                // Setup the spinner
                val arrayAdapter = this@Settings.let {
                    ArrayAdapter(
                        it,
                        R.layout.spinner_dark_item,
                        tempList.toArray()
                    )
                }
                spinner2.adapter = arrayAdapter
                println("LOG: Spinner 2 genres size is ${genres.size}")
                val initTopGenre2 = genres.indexOf(currTopGenre2)
                spinner2.setSelection(initTopGenre2)
                println("LOG: $currTopGenre1 with idx $initTopGenre2")
            }
        }
    }

}