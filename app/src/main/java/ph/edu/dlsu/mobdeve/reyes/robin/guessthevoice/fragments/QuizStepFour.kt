package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.TrackAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.callback.SwipeCallback
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepFourBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track
private const val ARG_PARAM1 = "quiz_name"
private const val ARG_PARAM2 = "genre"
private const val ARG_PARAM3 = "tracks"
private const val ARG_PARAM4 = "duration"

class QuizStepFour: Fragment() {
    private var _binding: QuizStepFourBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemTouchHelper: ItemTouchHelper

    private var quiz_name: String? = null
    private var genre: String? = null
    private var duration : Int? = null
    private var tracks: ArrayList<Track> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quiz_name = it.getString(ARG_PARAM1)
            genre = it.getString(ARG_PARAM2)
            tracks = it.getParcelableArrayList<Track>(ARG_PARAM3) as ArrayList<Track>
            duration = it.getInt(ARG_PARAM4)
        }
        println("STEP 4: oncreate ${quiz_name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepFourBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        binding.quizName.text = quiz_name
        binding.pubDuration.text = "${duration.toString()} sec."
        binding.genre.text = genre

        // Recycler View
        val ctx = getActivity()?.getApplicationContext()
        binding.pubTrackList.layoutManager = LinearLayoutManager(ctx)
        var trackAdapter = TrackAdapter(ctx, tracks)
        binding.pubTrackList.adapter = trackAdapter
//
//        var swipeCallback = SwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
//        swipeCallback.trackAdapter = trackAdapter
//        itemTouchHelper = ItemTouchHelper(swipeCallback)
//        itemTouchHelper.attachToRecyclerView(binding.pubTrackList)


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
//                    putString(ARG_PARAM1, "quiz_name")
                }
            }
    }
}