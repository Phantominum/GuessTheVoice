package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizMakerBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepFour
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepOne
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepThree
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepTwo
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track
import kotlin.reflect.typeOf

class QuizMaker : AppCompatActivity(), Communicator {
    private lateinit var binding: ActivityQuizMakerBinding
    private lateinit var quiz: Quiz
    private var quizBundle: Bundle = Bundle()
    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var currentFragIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentList.add(QuizStepOne.newInstance())
        fragmentList.add(QuizStepTwo())
        fragmentList.add(QuizStepThree())
        fragmentList.add(QuizStepFour())
        quiz = Quiz()
        // Start with first fragment
        replaceFragment(fragmentList[0], 0)
        binding.breadcrumbs.setText("Step ${currentFragIndex + 1} of 4")

        binding.fragment2Btn.setOnClickListener {
            if (currentFragIndex < 4)
            {
                currentFragIndex += 1
                if (currentFragIndex == 3) {
                    // set bundle argument
                    fragmentList[currentFragIndex].setArguments(quizBundle)
                    println("Last Step!!")
                    // set next button to Publish
                    binding.fragment2Btn.setText("Publish")
                    binding.fragment2Btn.setGravity(Gravity.CENTER)
                    binding.fragment2Btn.setTextColor(getResources().getColor(R.color.white))
                    binding.fragment2Btn.setBackgroundResource(R.drawable.round_button_bordered)
                    binding.breadcrumbs.setText("Step 4 of 4")
                    // pass bundle data
                    binding.fragment2Btn.setOnClickListener{
                        var goToDashboard = Intent(this, DashboardActivity::class.java)
                        startActivity(goToDashboard)
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

    override fun passData(data: Bundle, step: Int) {
        when (step) {
            1 -> {
                quiz.quiz_name = data.getString("quizName").toString()
                quiz.duration = data.getInt("duration")
                quiz.genre = data.getString("genre").toString()
                // Add to bundle for step 4
                quizBundle.putString("quiz_name", quiz.quiz_name)
                quizBundle.putString("genre", quiz.genre)
                quizBundle.putInt("duration", quiz.duration)
                println("STEP 1: ${quiz.quiz_name} ")
                println("STEP 1: ${quiz.duration} ")
                println("STEP 1: ${quiz.genre} ")
            }
            2 -> {
                quiz.tracks = data.getParcelableArrayList<Track>("tracks")!!
                // Add to bundle for step 4
                quizBundle.putParcelableArrayList("tracks", quiz.tracks)
                println("STEP 2: ${quiz.tracks.get(0).name}")
            }
            3 -> {
                quiz.quiz_image = data.getInt("quiz_image")
            }
        }
    }
}