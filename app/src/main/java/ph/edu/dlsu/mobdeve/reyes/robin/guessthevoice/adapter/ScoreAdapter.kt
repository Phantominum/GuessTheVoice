package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ScoreItemDesignBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.ScoreView

class ScoreAdapter(
    private var context:Context,
    private var scoreArray: ArrayList<ScoreView>,
    private var email:String
    ): RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(private val itemBinding: ScoreItemDesignBinding)
        :RecyclerView.ViewHolder(itemBinding.root) {
            fun bindScore(scoreView:ScoreView) {
                if (scoreView.score!!.username == email) {
                    itemBinding.rank.setTextColor(context.getResources().getColor(R.color.vibrant_pink))
                    itemBinding.username.setTextColor(context.getResources().getColor(R.color.vibrant_pink))
                    itemBinding.score.setTextColor(context.getResources().getColor(R.color.vibrant_pink))
                }
                itemBinding.rank.text = scoreView.score!!.rank.toString()
                itemBinding.username.text = scoreView.username
                itemBinding.score.text = scoreView.score!!.points.toString() + " pts."
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val itemBinding = ScoreItemDesignBinding.inflate(
            LayoutInflater.from(parent.context), // Loads layout during runtime
            parent, false       // What are these values for?
        )
        return ScoreViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bindScore(scoreArray[position])
    }

    override fun getItemCount(): Int {
        return scoreArray.size
    }

}
