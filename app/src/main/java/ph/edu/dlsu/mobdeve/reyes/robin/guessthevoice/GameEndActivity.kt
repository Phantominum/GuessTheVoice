package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.databinding.ActivityGameEndBinding
import java.io.File
import java.io.FileOutputStream

class GameEndActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameEndBinding
    private lateinit var email : String
    private lateinit var quizID : String
    private var points = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGameEndBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle = intent.extras

        email = bundle!!.getString("userEmail").toString()
        quizID = bundle.getString("quizID").toString()
        points = bundle.getInt("points")

        binding.textPoints.setText("${bundle!!.getInt("score").toString()} pts.")

        binding.buttonQuizEndBack.setOnClickListener{
            var gotoDashboard = Intent(this, DashboardActivity::class.java)
            startActivity(gotoDashboard)
        }

        binding.buttonQuizEndViewLeaderboards.setOnClickListener{
            var gotoLeaderboards = Intent(this, Leaderboards::class.java)
            bundle.putInt("score", points)
            bundle.putString("quizID", quizID)
            bundle.putString("email",email)
            gotoLeaderboards.putExtras(bundle)
            startActivity(gotoLeaderboards)
        }

        binding.shareFacebook.setOnClickListener{
            var layout = findViewById<View>(R.id.layout_game_end)
            var bitmap = createBitmap(layout.width, layout.height, config = Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            layout.draw(canvas)
            shareImageandText(bitmap)
        }
    }

    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getmageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image
        intent.type = "image/png"

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    // Retrieving the url to share
    private fun getmageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
}