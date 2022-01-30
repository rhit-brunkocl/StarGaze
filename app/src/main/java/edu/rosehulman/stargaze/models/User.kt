package edu.rosehulman.stargaze.models

data class User(var name: String="", var bio: String = "", var completedSetup: Boolean=false) {
    companion object{
        const val COLLECTION_PATH = "users"
    }

}