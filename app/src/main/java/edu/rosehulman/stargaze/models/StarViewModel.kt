package edu.rosehulman.stargaze.models
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class StarViewModel : ViewModel() {
    var results = ArrayList<Star>()
    var curUser = User()
    var all_stars = ArrayList<Star>()
    var selectedToView: Star? = null
    var currentPos = 0
    var ref = Firebase.firestore.collection(Star.COLLECTION_PATH)
    val subscriptions = HashMap<String, ListenerRegistration>()
    var criteria = SearchCriteria()
    init {
        for (i in 0 until 100){
            Firebase.firestore.collection("StarDatabase")
                .orderBy("id")
                .whereLessThan("id", 1400+i*1400)
                .whereGreaterThanOrEqualTo("id", 0+i*1400)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        addStar(document.toObject(Star::class.java))
                    }
                    Log.d("tag", "${all_stars.size} stars in all stars")
                }
        }
    }
    fun addStar(star: Star){
        all_stars.add(star)
    }
    fun removeListener(fragName: String){
        subscriptions[fragName]?.remove()
        subscriptions.remove(fragName)
    }

    fun addListener(fragName: String, useCriteria: Boolean, observer: () -> Unit) {
        if(!useCriteria){
           //use favorites from user
            Log.d("tag", curUser.favorites.size.toString())
            results = curUser.favorites
        }else{
            Log.d("tag", criteria.WDS_name.isNotEmpty().toString())
            Log.d("tag", criteria.WDS_name)
            if(criteria.WDS_name.isNotEmpty()){
                results.clear()
                for(stars in all_stars){
                    if(stars.WDSName == criteria.WDS_name){
                        results.add(stars)
                    }
                }
            }else {
                results.clear()
                for (star in all_stars) {
                    if (star.WDS_RA > criteria.min_RA && star.WDS_RA < criteria.max_RA
                        && star.WDS_DEC > criteria.min_Dec && star.WDS_DEC < criteria.max_Dec
                        && star.delta_sep > criteria.min_deltaSep && star.delta_sep < criteria.max_deltaSep
                        && star.delta_mag_GAIA > criteria.min_deltaMag && star.delta_mag_GAIA < criteria.max_deltaMag
                        && star.NOBS > criteria.min_obs && star.NOBS < criteria.max_obs
                        && star.LSTSEP > criteria.min_sep && star.LSTSEP < criteria.max_sep
                        && star.gaia_mag_1 > criteria.min_mag && star.gaia_mag_2 < criteria.max_mag
                        && star.FSTDATE > criteria.firstObs && star.LSTDATE < criteria.lastObs
                    ) {
                        if(curUser.settings.limit_search){
                            if(star.gaia_mag_1 < 10){
                                results.add(star)
                            }
                        }else {
                            results.add(star)
                        }
                    }
                }
                Log.d("tag","${results.size}")
            }

        }
    }

    fun clear() {
        all_stars.clear()
        results.clear()
    }

    fun favoriteStar(star: Star) {
        if(curUser.favorites.contains(star)){
            curUser.favorites.remove(star)
        }else {
            curUser.favorites.add(star)
        }
    }
    fun curStarToString() : String{
        return getCurStar().toString(curUser.settings)
    }
    fun getStarAt(pos: Int) = results[pos]
    fun getCurStar() = results[currentPos]
    fun updatePos(pos: Int){
        currentPos = pos
    }
    fun size() = results.size
}