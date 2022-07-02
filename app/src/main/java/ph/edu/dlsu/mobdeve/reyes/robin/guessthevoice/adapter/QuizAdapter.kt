package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.R
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.ViewQuizActivity
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.QuizCardViewDesignBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz



class QuizAdapter (

    ) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private lateinit var context: Context
    private lateinit var quizArray :ArrayList<Quiz>
    private var curr_user: String = ""
    private var likes : ArrayList<Quiz>? = ArrayList()

    override fun onBindViewHolder(holder:QuizAdapter. QuizViewHolder, position: Int) {
        holder.bindQuiz(quizArray[position])
    }

    constructor(context: Context,  quizArray: ArrayList<Quiz>, curr_user:String, likes:ArrayList<Quiz>?) : this() {
        this.context = context
        this.quizArray= quizArray
        this.curr_user = curr_user
        this.likes = likes
    }

    inner class QuizViewHolder(private val itemBinding: QuizCardViewDesignBinding)
        :RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        var quiz = Quiz()

        init{
            itemView.setOnClickListener(this)
        }
        fun bindQuiz(quiz: Quiz) {
            this.quiz = quiz
            if (quiz.quiz_creator == curr_user) {
                itemBinding.textQuizName.setTextColor(context.getResources().getColor(R.color.green))
                itemBinding.textQuizCreator.setTextColor(context.getResources().getColor(R.color.green))
                itemBinding.textLikes.setTextColor(context.getResources().getColor(R.color.green))
            }
            itemBinding.textQuizName.text = quiz.quiz_name
            itemBinding.textQuizCreator.text = quiz.quiz_creator
            itemBinding.textLikes.text = quiz.likes.toString()

            if (likes!!.indexOf(quiz) > -1)
                itemBinding.buttonQuizListLike.setImageResource(R.drawable.green_heart)
            itemBinding.quizImage.setImageResource(quiz.quiz_image)
            itemBinding.buttonQuizListLike
        }

        override fun onClick(v: View?){
            Toast.makeText(context, "${quiz.quiz_name}", Toast.LENGTH_SHORT).show()

            var goToQuiz = Intent(context, ViewQuizActivity::class.java)
            var bundle = Bundle()
            bundle.putString("email", curr_user)
            bundle.putString("quiz_name", quiz.quiz_name)
            bundle.putString("genre", quiz.genre)
            bundle.putString("description", quiz.description)
            bundle.putString("created_at", quiz.created_at)
            bundle.putString("quiz_creator", quiz.quiz_creator)
            bundle.putString("quiz_likes", quiz.likes.toString())
            bundle.putInt("quiz_image", quiz.quiz_image)

            goToQuiz.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            goToQuiz.putExtras(bundle)
            context.startActivity(goToQuiz)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAdapter.QuizViewHolder{
        val itemBinding = QuizCardViewDesignBinding.inflate(
            LayoutInflater.from(parent.context), // Loads layout during runtime
            parent, false       // What are these values for?
        )
        return QuizViewHolder(itemBinding)
    }



    override fun getItemCount(): Int {
        return quizArray.size
    }





    }




