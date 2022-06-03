package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizMakerBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepFour
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepOne
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepThree
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments.QuizStepTwo

class QuizMaker : AppCompatActivity() {
    lateinit var binding: ActivityQuizMakerBinding
    var fragmentList: ArrayList<Fragment> = ArrayList()
    var currentFragIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentList.add(QuizStepOne.newInstance())
        fragmentList.add(QuizStepTwo())
        fragmentList.add(QuizStepThree())
        fragmentList.add(QuizStepFour())
        replaceFragment(fragmentList[0])


        binding.fragment2Btn.setOnClickListener {
            println("At step ${currentFragIndex+1}")
            if (currentFragIndex == 3) {
                println("Last Step!!")
//                currentFragIndex = 0
                // set next button to Publish
                replaceFragment(fragmentList[currentFragIndex])
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
                replaceFragment(fragmentList[currentFragIndex])
                currentFragIndex += 1
                binding.breadcrumbs.setText("Step ${currentFragIndex} of 4")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}