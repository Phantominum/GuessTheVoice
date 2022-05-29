package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao


import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Quiz

interface QuizDAO {
    fun addQuiz(quiz: Quiz)
    fun getQuizzes():ArrayList<Quiz>
}

class QuizDAOArrayImpl: QuizDAO {
    private var quizList = ArrayList<Quiz>()
    override fun addQuiz(quiz: Quiz) {
        quizList.add(quiz)
    }

    override fun getQuizzes(): ArrayList<Quiz> {
        return quizList
    }

}