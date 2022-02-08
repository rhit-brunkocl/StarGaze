package edu.rosehulman.stargaze.models
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class StarViewModel : ViewModel() {
    var results = ArrayList<Star>()
    var curUser = User()
    var selectedToView = ArrayList<Star>()
    var currentPos = 0
    val ref = Firebase.firestore.collection(Star.COLLECTION_PATH)
    val subscriptions = HashMap<String, ListenerRegistration>()
    var criteria = SearchCriteria()
    fun setUser(user: User){
        curUser = user
    }
    fun removeListener(fragName: String){
        subscriptions[fragName]?.remove()
        subscriptions.remove(fragName)
    }
    fun addListener(fragName: String, useCriteria: Boolean, observer: () -> Unit) {
        if(!useCriteria){
           /* val subscription = ref
                .orderBy(Star.CREATED_KEY, Query.Direction.ASCENDING)
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
            */
        }else{
            if(criteria.WDS_name != ""){
                val subscription = ref
                    .orderBy(Star.CREATED_KEY, Query.Direction.ASCENDING)
                    .whereEqualTo("id", criteria.WDS_name)
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
            }else{
                val subscription = ref
                    .orderBy(Star.CREATED_KEY, Query.Direction.ASCENDING)
                    // implement filtering here
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