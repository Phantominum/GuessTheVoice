package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.Communicator
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.TrackAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.callback.SwipeCallback
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.TrackDAOArrayImpl
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepTwoBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track

class QuizStepTwo: Fragment() {
    private var _binding: QuizStepTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: Communicator
    private val dao = TrackDAOArrayImpl()
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var tracks: ArrayList<Track> = ArrayList()

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
        val ctx = getActivity()?.getApplicationContext()

        // Initialize communicator
        communicator = activity as Communicator

        // Recycler View
        binding.editTrackList.layoutManager = LinearLayoutManager(ctx)
        var trackAdapter = TrackAdapter(ctx, tracks, dao)
        binding.editTrackList.adapter = trackAdapter

        var swipeCallback = SwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeCallback.trackAdapter = trackAdapter
        itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.editTrackList)

        binding.btnAdd.setOnClickListener {
            var sample_track = Track("As It Was", "Harry Styles")
            trackAdapter.addTrack(sample_track)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bundle = Bundle()
        bundle.putParcelableArrayList("tracks",tracks)
        communicator.passData(bundle,2)
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
