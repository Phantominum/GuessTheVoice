package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizMakerBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepFour
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepOne
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepThree
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepTwo
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class QuizMaker : AppCompatActivity(), Communicator {
    private lateinit var binding: ActivityQuizMakerBinding
    private lateinit var quiz: Quiz
    private var quizBundle: Bundle = Bundle()
    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var currentFragIndex: Int = 0
    private lateinit var userID : String
    private lateinit var userEmail : String
    private lateinit var quizDAO : QuizDAO
    private lateinit var userDAO : UserDAO

    private lateinit var quiz_name : String
    private var duration : Int = 10
    private lateinit var genre: String
    private var quiz_image : Int = R.drawable.thumbnail1
    private var tracks : ArrayList<Track> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set dao
        quizDAO = QuizDAO(applicationContext)
        userDAO = UserDAO(applicationContext)
        // Retrieve user doc ID from bundle
        var bundle = intent.extras
        if (bundle != null) {
            // Get the user metadata
            userID = bundle.getString("userID").toString()
            userEmail = bundle.getString("email").toString()
            println("LOG: Quizmaker got $userID of $userEmail")
        } else {
            // TODO: Remove this when done testing
            userID = "Sn3T4P9vHj2JWh3Fl6Nd"
        }
        // Populate fragment list
        populateFragments()
        // Start with first fragment
        replaceFragment(fragmentList[0], 0)
        binding.breadcrumbs.setText("Step ${currentFragIndex + 1} of 4")

        binding.fragment2Btn.setOnClickListener {
            if (currentFragIndex < 4)
            {
                // Increment Index to change fragment
                currentFragIndex += 1
                // Update button if last fragment
                if (currentFragIndex == 3) {
                    // Set bundle argument
                    fragmentList[currentFragIndex].setArguments(quizBundle)
                    println("Last Step!!")
                    // Set next button to Publish
                    makePublishButton()
                    // Pass bundle data
                    binding.fragment2Btn.setOnClickListener{
                        createQuiz()
                    }
                }
                replaceFragment(fragmentList[currentFragIndex], currentFragIndex)
                binding.breadcrumbs.setText("Step ${currentFragIndex + 1} of 4")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, step: Int) {
        println("Replaced to index $step")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        if (step > 0)
            fragmentTransaction.remove(fragmentList[step-1]).commit()
        else
            fragmentTransaction.commit()
    }

    private fun makePublishButton() {
        binding.fragment2Btn.setText("Publish")
        binding.fragment2Btn.setGravity(Gravity.CENTER)
        binding.fragment2Btn.setTextColor(getResources().getColor(R.color.white))
        binding.fragment2Btn.setBackgroundResource(R.drawable.round_button_bordered)
        binding.breadcrumbs.setText("Step 4 of 4")
    }

    private fun populateFragments() {
        fragmentList.add(QuizStepOne.newInstance())
        fragmentList.add(QuizStepTwo())
        fragmentList.add(QuizStepThree())
        fragmentList.add(QuizStepFour())
    }

    private fun fieldsAreValid(): Boolean {
        //TODO: Add description
        return quiz_name.length == 0 || tracks.size == 0 || duration < 5 || duration > 30 || userEmail.length > 0
    }

    private fun createQuiz() {
        if (fieldsAreValid()) {
            val created_at = getCurrentDate()
            quiz = Quiz(quiz_name,userEmail,tracks,duration,genre,created_at,"")
            lifecycleScope.launch(Dispatchers.IO) {
                val quizID = async { quizDAO.createQuiz(quiz) }
                println("LOG: Quiz has been created with ID ${quizID.await()}")
                if (quizID.await() != null) {
                    val addToUser = async {
                        userDAO.addQuizToUser(userID, quizID.await()!!)
                    }
                    // Create intent if quiz has been added to user successfully
                    withContext(Dispatchers.Main) {
                        val dashBundle = Bundle()
                        dashBundle.putString("email", userEmail)
                        val goToDashboard = Intent(this@QuizMaker, DashboardActivity::class.java)
                        goToDashboard.putExtras(dashBundle)
                        startActivity(goToDashboard)
                        finish()
                    }
                }

            }
        }
    }

    fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val created_at: String = formatter.format(date)
        return created_at
    }

    override fun passData(data: Bundle, step: Int) {
        when (step) {
            1 -> {
                quiz_name = data.getString("quizName").toString()
                duration = data.getInt("duration")
                genre = data.getString("genre").toString()
                // Add to bundle for step 4
                quizBundle.putString("quiz_name", quiz_name)
                quizBundle.putString("genre", genre)
                quizBundle.putInt("duration", duration)
                println("STEP 1: ${quiz_name} ")
                println("STEP 1: ${duration} ")
                println("STEP 1: ${genre} ")
            }
            2 -> {
                tracks = data.getParcelableArrayList<Track>("tracks")!!
                // Add to bundle for step 4
                quizBundle.putParcelableArrayList("tracks", tracks)
                println("STEP 2: ${tracks.size}")
            }
            3 -> {
                quiz_image = data.getInt("quiz_image")
            }
        }
    }
}