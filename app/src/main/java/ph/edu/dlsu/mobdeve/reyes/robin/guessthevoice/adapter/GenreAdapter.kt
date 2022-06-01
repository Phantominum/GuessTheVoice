package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.GenreCardViewDesignBinding

import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre
import kotlin.random.Random


class GenreAdapter (
    private var context: Context,
    private var genreArray: ArrayList<Genre>,

    ): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(private val itemBinding: GenreCardViewDesignBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun  bindGenre(genre: Genre) {
            var genre_colors = IntArray(13)

            genre_colors = context.getResources().getIntArray(R.array.genre_colors)
            var randomColorGen = genre_colors[Random.nextInt(genre_colors.size)]
            itemBinding.textGenreCard.text = genre.genre_name
            itemView.setBackgroundColor(randomColorGen)
//            itemBinding.genreCard.setBackgroundColor(0x4CAF50)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.GenreViewHolder{
        val itemBinding = GenreCardViewDesignBinding.inflate(
            LayoutInflater.from(parent.context), // Loads layout during runtime
            parent, false       // What are these values for?
        )
        return GenreViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder:GenreAdapter. GenreViewHolder, position: Int) {
        holder.bindGenre(genreArray[position])
    }

    override fun getItemCount(): Int {
        return genreArray.size
    }

}
