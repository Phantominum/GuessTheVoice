package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.Communicator
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepThreeBinding

class QuizStepThree: Fragment() {
    private var _binding: QuizStepThreeBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator
    private var thumbnail: Int = R.drawable.thumbnail1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepThreeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        // Initialize communicator
        communicator = activity as Communicator
        // Set click listeners
        binding.thumbnail1.setOnClickListener {
            thumbnail = R.drawable.thumbnail1
            println("LOG: Selected $thumbnail")
        }
        binding.thumbnail2.setOnClickListener {
            thumbnail = R.drawable.thumbnail2
            println("LOG: Selected $thumbnail")
        }
        binding.thumbnail3.setOnClickListener {
            thumbnail = R.drawable.thumbnail3
            println("LOG: Selected $thumbnail")
        }
        binding.thumbnail4.setOnClickListener {
            thumbnail = R.drawable.thumbnail1
            println("LOG: Selected $thumbnail")
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bundle = Bundle()
        println("LOG: Thumbnail to pass is $thumbnail")
        bundle.putInt("quiz_image",thumbnail)
        communicator.passData(bundle, 3)
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