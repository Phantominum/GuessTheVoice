package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizCardViewDesignBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz



class QuizAdapter (
    private var context: Context,
    private var quizArray: ArrayList<Quiz>,
    private var curr_user:String
    ) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(private val itemBinding: QuizCardViewDesignBinding)
        :RecyclerView.ViewHolder(itemBinding.root) {
        fun bindQuiz(quiz: Quiz) {
            if (quiz.quiz_creator == curr_user) {
                itemBinding.textQuizName.setTextColor(context.getResources().getColor(R.color.green))
                itemBinding.textQuizCreator.setTextColor(context.getResources().getColor(R.color.green))
                itemBinding.textLikes.setTextColor(context.getResources().getColor(R.color.green))
            }
            itemBinding.textQuizName.text = quiz.quiz_name
            itemBinding.textQuizCreator.text = quiz.quiz_creator
            itemBinding.textLikes.text = quiz.likes.toString()
            itemBinding.quizImage.setImageResource(quiz.quiz_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAdapter.QuizViewHolder{
        val itemBinding = QuizCardViewDesignBinding.inflate(
            LayoutInflater.from(parent.context), // Loads layout during runtime
            parent, false       // What are these values for?
        )
        return QuizViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder:QuizAdapter. QuizViewHolder, position: Int) {
        holder.bindQuiz(quizArray[position])
    }

    override fun getItemCount(): Int {
        return quizArray.size
    }

    }



