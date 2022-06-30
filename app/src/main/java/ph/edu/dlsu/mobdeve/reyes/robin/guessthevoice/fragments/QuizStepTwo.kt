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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.Communicator
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter.TrackAdapter
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.callback.SwipeCallback
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.TrackDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizStepTwoBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track

private const val ARG_PARAM2 = "genre"

class QuizStepTwo: Fragment() {
    private var _binding: QuizStepTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var ctx: Context
    private lateinit var communicator: Communicator
    private lateinit var trackDAO : TrackDAO
    private var genre: String = "None"

    private lateinit var spinner : Spinner
    private var selectedTrack: Int = -1
    private var tracks: ArrayList<Track> = ArrayList()
    private var tracksID: ArrayList<String> = ArrayList()
    private var selectedTracks: ArrayList<Track> = ArrayList()
    private var selectedTracksID: ArrayList<String> = ArrayList()
    private var songnames: ArrayList<String> = ArrayList()

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre = it.getString(ARG_PARAM2).toString()
        }
        // TODO: Remove when done testing
        println("LOG: Step TWO received genre ${genre}")
        ctx = requireActivity().getApplicationContext()
        trackDAO = TrackDAO(ctx)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = QuizStepTwoBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val ctx = getActivity()?.getApplicationContext()

        spinner = binding.dropdownTrack
        setupSpinner()

        // Initialize communicator
        communicator = activity as Communicator

        // Recycler View
        binding.editTrackList.layoutManager = LinearLayoutManager(ctx)
        var trackAdapter = TrackAdapter(ctx, tracks)
        binding.editTrackList.adapter = trackAdapter

        var swipeCallback = SwipeCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeCallback.trackAdapter = trackAdapter
        itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.editTrackList)

        binding.btnAdd.setOnClickListener {
            if (selectedTrack != -1) {
                trackAdapter.addTrack(tracks.get(selectedTrack))
                // Add selected track metadata
                selectedTracks.add(tracks.get(selectedTrack))
                // Add selected track ID
                selectedTracksID.add(tracksID.get(selectedTrack))
            }
        }

        return view
    }

    private fun setupSpinner() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Retrieve song metadata
            val choices = async { trackDAO.getTracks(genre) }
            // Retrieve song IDs
            val choicesIDs = async { trackDAO.getTracksID(genre) }
            withContext(Dispatchers.Main) {
                // Extract the names and metadata
                for (value in choices.await()!!) {
                    songnames.add(value.SongName!!)
                    tracks.add(value)
                }
                // Extract the song IDs
                for (value in choicesIDs.await()!!) {
                    tracksID.add(value)
                }
                // Setup the spinner
                val arrayAdapter = ctx.let {
                    ArrayAdapter(
                        it,
                        R.layout.spinner_item,
                        songnames.toArray()
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
                selectedTrack = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bundle = Bundle()
        bundle.putParcelableArrayList("tracks",selectedTracks)
        bundle.putStringArrayList("tracksID",selectedTracksID)
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
