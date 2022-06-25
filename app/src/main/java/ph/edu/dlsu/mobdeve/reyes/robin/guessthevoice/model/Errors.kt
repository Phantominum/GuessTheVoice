package ph.edu.dlsu.mobdeve.reyes.robin.guessthevoice.model

abstract class Errors(m :String) {
    var message : String = m
    abstract fun getErrorMessage(): String
}

class InvalidUsername(): Errors("Username has invalid characters") {
    override fun getErrorMessage(): String {
        return super.message
    }
}

class FirebaseError(var m: String) : Errors(m) {
    override fun getErrorMessage(): String {
        return super.message
    }
}


class InputError(var m: String) : Errors(m) {
    override fun getErrorMessage(): String {
        return super.message
    }
}