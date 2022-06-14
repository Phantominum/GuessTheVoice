package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizMakerBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepFour
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepOne
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepThree
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepTwo
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

class QuizMaker : AppCompatActivity(), Communicator {
    private lateinit var binding: ActivityQuizMakerBinding
    private lateinit var quiz: Quiz
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
            println("At step ${currentFragIndex+1}")
            if (currentFragIndex == 3) {
                println("Last Step!!")
                // set next button to Publish
                replaceFragment(fragmentList[currentFragIndex], currentFragIndex)
                binding.fragment2Btn.setText("Publish")
                binding.fragment2Btn.setGravity(Gravity.CENTER)
                binding.fragment2Btn.setTextColor(getResources().getColor(R.color.white))
                binding.fragment2Btn.setBackgroundResource(R.drawable.round_button_bordered)
                binding.breadcrumbs.setText("Step 4 of 4")
                binding.fragment2Btn.setOnClickListener{
                    var goToDashboard = Intent(this, DashboardActivity::class.java)
                    startActivity(goToDashboard)
                }
            }
            else {
                currentFragIndex += 1
                replaceFragment(fragmentList[currentFragIndex], currentFragIndex)
                binding.breadcrumbs.setText("Step ${currentFragIndex + 1} of 4")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, step: Int) {
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
                println("STEP 1: ${quiz.quiz_name} ")
                println("STEP 1: ${quiz.duration} ")
                println("STEP 1: ${quiz.genre} ")
            }
        }
//        val bundle = Bundle()
//        bundle.putString("title",editTextData)
//        val transaction = this.supportFragmentManager.beginTransaction()
//        val fragB = fragmentList[1]
//        transaction.replace(R.id.fragmentContainer, fragB)
//        transaction.commit()
    }
}