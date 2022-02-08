package edu.rosehulman.stargaze.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Star(var name: String, var RA:String, var Dec:String, var sep:Float, var delta_sep:Float, var magnitude:Float, var delta_mag:Float, var num_obs:Int, var last_year_obs:Int,var first_year_obs: Int, var parallax: Float, var harshaw: Float){
    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    fun toString(settings: Setting): String {
        var out = ""
        out+= "RA: $RA\nDec: $Dec\nSeparation: $sep\nDelta Separation: $delta_sep\nMagnitude of A star: $magnitude\nDelta magnitude: $delta_mag\nNumber of Observations: $num_obs\nFirst observed: $first_year_obs\nLast observed: $last_year_obs\n"
        if(settings.parallax){
            out+="Parallax: $parallax\n"
        }else if(settings.harshaw){
            out+="Harshaw Statistic: $harshaw\n"
        }
        return out
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
