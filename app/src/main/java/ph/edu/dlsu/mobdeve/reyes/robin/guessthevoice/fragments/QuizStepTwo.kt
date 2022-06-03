package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepTwoBinding

class QuizStepTwo: Fragment() {
    private var _binding: QuizStepTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepTwoBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        //        binding.lblDuration.setTextColor(getResources().getColor(R.color.vibrant_pink))
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QuizStepOne().apply {
                arguments = Bundle().apply {
                    //                putString(ARG_PARAM1, param1)
                }
            }
    }
}