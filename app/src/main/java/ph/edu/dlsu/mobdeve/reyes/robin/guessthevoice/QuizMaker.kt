package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityQuizMakerBinding
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

        fragmentList.add(QuizStepOne())
        fragmentList.add(QuizStepTwo())
        fragmentList.add(QuizStepThree())


        binding.fragment2Btn.setOnClickListener {
            replaceFragment(fragmentList[currentFragIndex])
            if (currentFragIndex == 2) {
                currentFragIndex = 0
            } else
                currentFragIndex += 1
            binding.breadcrumbs.setText("Step ${currentFragIndex+1} of 4")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}