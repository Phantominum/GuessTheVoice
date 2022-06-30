package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.Communicator
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.GenreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepOneBinding

class QuizStepOne : Fragment() {
    private var _binding: QuizStepOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var genreDAO: GenreDAO
    private lateinit var communicator: Communicator
    private lateinit var spinner : Spinner
    private lateinit var selectedGenre: String
    private var genres = ArrayList<String>()
    private lateinit var ctx: Context
    private var duration: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ctx = requireActivity().getApplicationContext()
        genreDAO = GenreDAO(ctx)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepOneBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        spinner = binding.dropdownGenre
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
        lifecycleScope.launch(Dispatchers.IO) {
            // Retrieve genre names
            val choices = async { genreDAO.getGenres() }
            withContext(Dispatchers.Main) {
                // Extract the names
                for (value in choices.await()!!)
                    genres.add(value.genre_name)
                // Setup the spinner
                val arrayAdapter = ctx.let {
                    ArrayAdapter(
                        it,
                        R.layout.spinner_item,
                        genres.toArray()
                    )
                }
                spinner.adapter = arrayAdapter
            }
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    ctx, "Selected genre " + genres.get(position),
                    Toast.LENGTH_SHORT
                ).show()
                selectedGenre = genres.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    override fun onStop() {
        super.onStop()
        println("STOP")
        val bundle = Bundle()
        bundle.putString("quizName", binding.etQuizName.text.toString())
        bundle.putInt("duration", duration)
        bundle.putString("description", binding.etDescription.text.toString())
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
