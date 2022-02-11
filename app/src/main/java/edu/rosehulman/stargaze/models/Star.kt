package edu.rosehulman.stargaze.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Star(
    var WDSName: String = "",
    var WDS_disc: String = "",
    var components: String = "",
    var gaia_PMRA1: Double = 0.0,
    var gaia_PMDEC1: Double = 0.0,
    var parallax_A: Double = 0.0,
    var parallax_b: Double = 0.0,
    var d_pri: Double = 0.0,
    var d_sec: Double = 0.0,
    var gaia_mag_1: Double = 0.0,
    var gaia_mag_2: Double = 0.0,
    var gaia_sep:Double = 0.0,
    var gaia_pa: Double = 0.0,
    var dist_prob: Double = 0.0,
    var Avg_PM_Vect: Double = 0.0,
    var PM_Prob: Double = 0.0,
    var Binary_Prob: Double = 0.0,
    var physical: String = "",
    var LSTDATE:Int = 0,
    var FSTDATE: Int = 0,
    var NOBS:Int = 0,
    var FSTPA: Double = 0.0,
    var LSTPA: Double = 0.0,
    var FSTSEP: Double = 0.0,
    var LSTSEP: Double = 0.0,
    var FSTMAG: Double = 0.0,
    var SECMAG: Double = 0.0,
    var STYPE: String = "",
    var PM1RA: Double = 0.0,
    var PM1DC: Double = 0.0,
    var PM2RA: Double = 0.0,
    var PM2DC: Double = 0.0,
    var NOTES: String = "",
    var WDS_RA:Double = 0.0,
    var WDS_DEC:Double = 0.0,
    var harshaw: Double = 0.0,
    var delta_sep:Double = 0.0,
    var delta_PA: Double = 0.0,
    var delta_mag_GAIA:Double = 0.0,
    var id: Int = 0
){
    @get:Exclude

    @ServerTimestamp
    var created: Timestamp? = null

    fun toString(settings: Setting): String {
        var out = ""
        out+= "RA: $WDS_RA\nDec: $WDS_DEC\nSeparation: $gaia_sep\nDelta Separation: $delta_sep\nMagnitude of A star: $gaia_mag_1\nMagnitude of B star: ${gaia_mag_2}\nDelta magnitude: $delta_mag_GAIA\nNumber of Observations: $NOBS\nFirst observed: $FSTDATE\nLast observed: $LSTDATE\n"
        if(settings.parallax){
            out+="Parallax: $parallax_A and $parallax_b\n"
        }else if(settings.harshaw){
            out+="Harshaw Statistic: $harshaw\n"
        }
        return out
    }

    companion object {
        const val COLLECTION_PATH = "StarDatabase"
        const val CREATED_KEY= "created"
        fun from(snapshot: DocumentSnapshot): Star {
            val star = snapshot.toObject(Star::class.java)!! // data
            return star
        }
    }
}
