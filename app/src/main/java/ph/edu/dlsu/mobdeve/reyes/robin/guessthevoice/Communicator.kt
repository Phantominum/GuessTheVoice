package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice

import android.os.Bundle

interface Communicator {
    fun passData(data : Bundle, step: Int)
}