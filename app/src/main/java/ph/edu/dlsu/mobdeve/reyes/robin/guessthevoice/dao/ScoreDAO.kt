package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Score

interface ScoreDAO {
    fun addScore(score: Score)
    fun getScores():ArrayList<Score>
}

class ScoreDAOArrayImpl: ScoreDAO {
    private var scoresList = ArrayList<Score>()
    override fun addScore(score: Score) {
        scoresList.add(score)
    }

    override fun getScores(): ArrayList<Score> {
        return scoresList
    }

}