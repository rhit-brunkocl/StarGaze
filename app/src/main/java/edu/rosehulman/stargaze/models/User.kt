package edu.rosehulman.stargaze.models

data class User(var name: String="", var bio: String = "", var completedSetup: Boolean=false, var settings: Setting=Setting(), var favorites: ArrayList<Star> = arrayListOf<Star>()) {
    companion object{
        const val COLLECTION_PATH = "users"
    }

}