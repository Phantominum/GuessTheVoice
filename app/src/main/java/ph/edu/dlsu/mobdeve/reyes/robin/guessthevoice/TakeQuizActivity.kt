package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.ContentValues
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao.ScoreDAO
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityTakeQuizBinding
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score
import java.io.IOException

class TakeQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakeQuizBinding
    private lateinit var scoredao : ScoreDAO

    val db = Firebase.firestore
    var listOfSongs = arrayOf<String>()
    var listOfAnswers = arrayOf<String>()
    var songIds = arrayListOf<Int>()
    var currSong = 0
    var points = 0
    var quizTime =0
    var quizID = ""
    var email = ""

    val delayTimer =
        object: CountDownTimer(3000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if ((millisUntilFinished / 1000) == 2L) {
                    binding.timer.setText("Ready?")
                } else if ((millisUntilFinished / 1000) == 1L) {
                    binding.timer.setText("Set")
                }
            }

            override fun onFinish() {
                playAudioTimed()
            }
        }

    var mediaPlayer : MediaPlayer?  = null

    private fun getSongs(){

        db.collection("Songs")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
//                    print("${document.data}")
//                    binding.textTrackNum.setText("${document.data.get("Artist")}")\
                    listOfSongs+="${document.data.get("AudioURL")}"
                    listOfAnswers+="${document.data.get("Artist")}"
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }

    private fun getQuiz(){
        println("QUIZ ID $quizID")
        db.collection("Quizzes")
            .document(quizID)
            .get().addOnSuccessListener { doc->
                quizTime = doc.data?.get("duration").toString().toInt()
                songIds = doc.data!!.get("tracks") as ArrayList<Int>

                getSongsFromQuiz()
            }

    }

    private fun getSongsFromQuiz(){
        db.collection("Songs")
            .whereIn(FieldPath.documentId(), songIds)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    print("${document.data}")
//                    binding.textTrackNum.setText("${document.data.get("Artist")}")\
                    listOfSongs+="${document.data.get("AudioURL")}"
                    listOfAnswers+="${document.data.get("Artist")}"
                }

                binding.startButton.isEnabled = true

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTakeQuizBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent.extras != null){
            val bundle = intent.extras
            email = bundle!!.getString("userEmail").toString()
            quizID = bundle.getString("quizID").toString()
            println("Received quiz ID: ${quizID}")
        } else {
            println("No quiz ID received")
        }
        getQuiz()
        binding.startButton.isEnabled = false

//        getSongs()



        binding.startButton.setOnClickListener{
            startDelayTimer()
            binding.startButton.visibility = View.GONE

        }


        binding.textAnswerField.setOnKeyListener(View.OnKeyListener{
                _,keycode, event-> if(keycode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
            playNextTrack()
            binding.textAnswerField.setText("")
            binding.textAnswerField.isEnabled = false
        }
            false
        })

//        binding.startButton.setOnClickListener{
//
//            var goToGameEnd = Intent(this, GameEndActivity::class.java)
//            startActivity(goToGameEnd)
//        }

    }

    private fun playNextTrack(){
        //check if the answer is correct
        //add the points to the score if it is correct

        val userAnswer = binding.textAnswerField.getText().toString()
        val correctAnswer = listOfAnswers[currSong]
        if(userAnswer == correctAnswer){
            points +=10
        }


//        //stop the track
//        mediaPlayer!!.stop()
//        mediaPlayer!!.release()
//
//        //stop the timer
//        delayTimer.cancel()
//
//        //increment index of quiz
//        currSong+=1
//
//        //play the next track
//        startDelayTimer()
    }

    private fun startDelayTimer(){
        if(currSong<listOfSongs.size) {
            binding.textTrackNum.setText("Track "+ (currSong+1))
            delayTimer.start()
        }
        else{
            lifecycleScope.launch(Dispatchers.IO) {
                addToLeaderboard(points, quizID, email)
            }
        }
    }

    private fun startTimer(){
        object : CountDownTimer(quizTime*1000L, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if((millisUntilFinished/1000) ==quizTime-1L){
                    binding.timer.setText("Go!")
                }
                else {
                    binding.timer.setText("" + millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                binding.textAnswerField.isEnabled = false
                binding.timer.setText("Done!")
                binding.textAnswerField.isEnabled = true


            }
        }.start()
    }

    private fun playAudioTimed(){
        if(currSong<listOfSongs.size) {

            val audioURL = listOfSongs[currSong]
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer!!.setDataSource(audioURL)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                startTimer()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val timer = object : CountDownTimer(quizTime*1000L, 1000) {

                override fun onTick(p0: Long) {
                    //do nothing
                }

                override fun onFinish() {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()
                    currSong+=1
                    startDelayTimer()
                    binding.scoreLabel.text = ("$points pts.")
                }
            }
            timer.start()
        }
    }
    private suspend fun addToLeaderboard(points: Int, quizID: String,username: String){

        var res = db.collection("Scores").whereEqualTo("username", email).get().await()
        if(res.documents.size>0){
            db.collection("Scores").document(res.documents[0].id).update("points", points)
            val goToScorePageActivity = Intent(this, GameEndActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("score", points)
            goToScorePageActivity.putExtras(bundle)
            startActivity(goToScorePageActivity)
        }
        else {
            db.collection("Scores").add(Score(points, quizID, username))
                .addOnSuccessListener {
                    val goToScorePageActivity = Intent(this, GameEndActivity::class.java)
                    val bundle = Bundle()
                    bundle.putInt("score", points)
                    goToScorePageActivity.putExtras(bundle)
                    startActivity(goToScorePageActivity)
                }
        }
    }


}