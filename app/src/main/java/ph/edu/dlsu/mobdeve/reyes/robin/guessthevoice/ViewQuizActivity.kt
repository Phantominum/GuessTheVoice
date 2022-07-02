package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.QuizDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.UserDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityViewQuizBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

class ViewQuizActivity : AppCompatActivity() {
    private lateinit var binding:ActivityViewQuizBinding
    private lateinit var userDAO: UserDAO
    private lateinit var quizDAO: QuizDAO
    private var likedQuizzes : ArrayList<String>? = ArrayList()

    private lateinit var quiz_name : String
    private lateinit var quiz_genre : String
    private lateinit var quiz_created_at : String
    private lateinit var quiz_creator : String
    private var liked_state = false
    private var likes = 0
    private var userEmail: String? = null
    private var quizID : String? = null
    private var fullQuizData : Quiz? = Quiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Load DAO
        userDAO = UserDAO(applicationContext)
        quizDAO = QuizDAO(applicationContext)
        // Retrieve bundle
        val bundle = intent.extras
        userEmail = bundle!!.getString("email")
        quiz_name = bundle.getString("quiz_name").toString()
        quiz_genre = bundle.getString("genre").toString()
        quiz_created_at = bundle.getString("created_at").toString()
        quiz_creator = bundle.getString("quiz_creator").toString()
        binding.textViewQuizName.text = "${quiz_name}"
        binding.textViewQuizCreator.text = "${quiz_creator}"
        binding.viewQuizImage.setImageResource(bundle.getInt("quiz_image"))
        // Set quiz likes
        if (userEmail != null) {
            setQuizLikes(userEmail!!)
        }


        binding.buttonViewLeaderboards.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO){
                 var leaderboardQuizID : String? = null
                val goToLeaderboards = Intent(applicationContext, Leaderboards::class.java)
                val quizJob = async { quizDAO.getQuizMetadataID(quiz_name,quiz_genre, quiz_created_at, quiz_creator) }
                leaderboardQuizID = quizJob.await()
                val bundle = Bundle()
                bundle.putString("quizID", leaderboardQuizID)
                bundle.putString("email", userEmail)
                goToLeaderboards.putExtras(bundle)
                startActivity(goToLeaderboards)
            }

        }


        binding.buttonLikeQuiz.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                // Retrieve likes
                val likeJob = async { quizDAO.getLikes(quizID!!) }
                likes = likeJob.await()
                // Continue updating likes if result is non negative
                withContext(Dispatchers.Main) {
                    if (likes != -1) {
                        if (liked_state == true) {
                            binding.buttonLikeQuiz.setImageResource(R.drawable.heart)
                            likes -= 1
                            // Tell DB to subtract
                            val minusJob = async { quizDAO.updateLikes(quizID!!, likes)}
                            likes = minusJob.await()
                            println("LOG: Subtract from ${likes+1} to $likes")
                            // Remove from user liked
                            val userID = async { userDAO.getAccountDocID(userEmail!!) }
                            async { userDAO.removeQuizToLiked(userID.await()!!, quizID!!) }
                            liked_state = false
                        } else {
                            binding.buttonLikeQuiz.setImageResource(R.drawable.green_heart)
                            likes += 1
                            // Tell DB to add
                            val addJob = async { quizDAO.updateLikes(quizID!!, likes)}
                            likes = addJob.await()
                            println("LOG: Add from ${likes-1} to $likes")
                            // Add to user liked
                            val userID = async { userDAO.getAccountDocID(userEmail!!) }
                            async { userDAO.addQuizToLiked(userID.await()!!, quizID!!) }
                            liked_state = true
                        }
                    }
                }


            }

//            if(!liked_state){
//                binding.buttonLikeQuiz.setImageResource(R.drawable.green_heart)
//                liked_state = true
//                println("LOG: Set to $liked_state")
//            }
//            else{
//                binding.buttonLikeQuiz.setImageResource(R.drawable.heart)
//                liked_state = false
//                println("LOG: Set to $liked_state")
//            }
        }
    }


    private fun setQuizLikes(email : String) {
        lifecycleScope.launch(Dispatchers.IO) {
            // TODO: Get list of liked quizzes from user
            val userLikedJob = async {userDAO.getLikedQuizzes(email)}
            likedQuizzes = userLikedJob.await()
            if (likedQuizzes != null)
                println("LOG: User has liked ${likedQuizzes!!.size} quizzes")
            // TODO: Get quiz ID
            println("LOG: Created at is $quiz_created_at")
            val quizJob = async { quizDAO.getQuizMetadataID(quiz_name,quiz_genre, quiz_created_at, quiz_creator) }
            quizID = quizJob.await()
            println("LOG: View Quiz with ID ${quizID}")
            // Get number of likes
            val quizDataJob = async { quizDAO.getQuizById(quizID!!)}
            fullQuizData = quizDataJob.await()
            likes = fullQuizData!!.likes
            // Set a click listener only if quiz exists
            if (quizID != null)
            {
                liked_state = true && likedQuizzes!!.any{ it == quizID }
                println("LOG: Initial state is $liked_state")

                withContext(Dispatchers.Main) {
                    if (liked_state)
                        binding.buttonLikeQuiz.setImageResource(R.drawable.green_heart)
                }
            }

            withContext(Dispatchers.Main) {
                binding.buttonPlayQuiz.setOnClickListener{
                    // Pass the Quiz ID
                    val playBundle = Bundle()
                    playBundle.putString("userEmail", userEmail)
                    playBundle.putString("quizID", quizID)
//                    playBundle.putStringArrayList("tracks", fullQuizData!!.tracks)
                    val goToTakeQuiz = Intent(this@ViewQuizActivity, TakeQuizActivity::class.java)
                    goToTakeQuiz.putExtras(playBundle)
                    startActivity(goToTakeQuiz)
                }
            }

        }
    }
}