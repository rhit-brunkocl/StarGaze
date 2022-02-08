package edu.rosehulman.stargaze.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Star(var name: String, var RA:String, var Dec:String, var sep:Float, var delta_sep:Float, var magnitude:Float, var delta_mag:Float, var num_obs:Int, var last_year_obs:Int){
    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    fun toString(settings: Setting): String {
        return ""
    }

    companion object {
        const val COLLECTION_PATH = "StarData"
        const val CREATED_KEY= "created"
        fun from(snapshot: DocumentSnapshot): Star {
            val star = snapshot.toObject(Star::class.java)!! // data
            star.id = snapshot.id
            return star
        }
    }
}
