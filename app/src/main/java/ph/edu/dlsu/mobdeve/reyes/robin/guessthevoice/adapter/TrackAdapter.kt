package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizmakerTrackBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Track

class TrackAdapter(
    private var context: Context?,
    private var trackArray: ArrayList<Track>
): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(private val itemBinding: QuizmakerTrackBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
            fun  bindTrack(track: Track) {
                itemBinding.trackArtist.setText(track.Artist)
                itemBinding.trackName.setText(track.SongName)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemBinding = QuizmakerTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TrackViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bindTrack(trackArray[position])
    }

    override fun getItemCount(): Int {
        return trackArray.size
    }

    fun removeTrack(position: Int) {
        trackArray.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addTrack(track: Track) {
        trackArray.add(0,track)
        notifyItemInserted(0)
    }
}