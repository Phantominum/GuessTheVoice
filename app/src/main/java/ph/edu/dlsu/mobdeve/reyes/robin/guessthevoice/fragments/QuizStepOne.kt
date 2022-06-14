package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.Communicator
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepOneBinding

class QuizStepOne : Fragment() {
    private var _binding: QuizStepOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator
    private lateinit var selectedGenre: String
    private var duration: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepOneBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        setupSpinner()

        // Initialize communicator
        communicator = activity as Communicator

        binding.btnAdd.setOnClickListener{
            // Max is 120 seconds (2 mins)
            if (duration < 120)
                duration += 1
            binding.duration.text = duration.toString()
        }

        binding.btnMinus.setOnClickListener{
            // Min is 5 seconds
            if (duration > 5)
                duration -= 1
            binding.duration.text = duration.toString()
        }
        return view
    }

    private fun setupSpinner() {
        val genres = arrayOf("Jazz","Pop","Classical")
        val spinner = binding.dropdownGenre
        val ctx = getActivity()?.getApplicationContext()
        val arrayAdapter = ctx?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_item,
                genres
            )
        }
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    ctx, "Selected genre " + genres[position],
                    Toast.LENGTH_SHORT
                ).show()
                selectedGenre = genres[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bundle = Bundle()
        bundle.putString("quizName", binding.etQuizName.getText().toString())
        bundle.putInt("duration", duration)
        bundle.putString("genre", selectedGenre)
        communicator.passData(bundle, 1)
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
