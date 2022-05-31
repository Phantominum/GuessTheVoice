package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.dao

import ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model.Genre

interface GenreDAO {
    fun addGenre(genre: Genre)
    fun getGenres():ArrayList<Genre>
}

class GenreDAOArrayImpl: GenreDAO {
    private var genreList = ArrayList<Genre>()
    override fun addGenre(genre: Genre) {
        genreList.add(genre)
    }

    override fun getGenres(): ArrayList<Genre> {
        return genreList

    }

}