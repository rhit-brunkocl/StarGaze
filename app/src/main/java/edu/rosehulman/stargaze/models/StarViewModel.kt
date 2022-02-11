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
    var selectedToView = ArrayList<Star>()
    var currentPos = 0
    var ref = Firebase.firestore.collection(Star.COLLECTION_PATH)
    val subscriptions = HashMap<String, ListenerRegistration>()
    var criteria = SearchCriteria()
    val uid = Firebase.auth.uid!!
    fun setUser(user: User){
        curUser = user
    }
    fun addStar(star: Star){
        results.add(star)
    }
    fun removeListener(fragName: String){
        subscriptions[fragName]?.remove()
        subscriptions.remove(fragName)
    }
    fun addListener(fragName: String, useCriteria: Boolean, observer: () -> Unit) {
        if(!useCriteria){
           //use favorites from user
            results = curUser.favorites
        }else{
            if(criteria.WDS_name != ""){
                val subscription = ref
                    .orderBy("id", Query.Direction.ASCENDING)
                    .whereEqualTo("WDSName", criteria.WDS_name)
                    .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                        error?.let {
                            Log.d("Tag", "Error: $it")
                            return@addSnapshotListener
                        }
                        Log.d("tag", "In snapshot listener with ${snapshot?.size()} docs")
                        clear()
                        snapshot?.documents?.forEach {
                            results.add(Star.from(it))
                        }
                        observer()
                    }
                subscriptions[fragName] = subscription
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
                        results.add(star)
                    }
                }
            }

        }
    }

    fun clear() {
        results.clear()
    }

    fun favoriteStar(star: Star) {
        curUser.favorites.add(star)
    }
    fun curStarToString() : String{
        return getCurStar().toString(curUser.settings)
    }
    fun getStarAt(pos: Int) = results[pos]
    fun getCurStar() = results[currentPos]
    fun unfavoriteCurStar(){
        curUser.favorites.remove(getCurStar())
    }
    fun updatePos(pos: Int){
        currentPos = pos
    }
    fun size() = results.size
}