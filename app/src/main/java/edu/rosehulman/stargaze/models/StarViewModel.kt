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
    var favorites = ArrayList<Star>()
    var settings = Setting()
    var selectedToView = ArrayList<Star>()
    var currentPos = 0
    val ref = Firebase.firestore.collection(Star.COLLECTION_PATH)
    val subscriptions = HashMap<String, ListenerRegistration>()
    fun removeListener(fragName: String){
        subscriptions[fragName]?.remove()
        subscriptions.remove(fragName)
    }
    fun addListener(fragName: String, observer: () -> Unit) {
        val subscription = ref
            .orderBy(Star.CREATED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d("Tag", "Error: $it")
                    return@addSnapshotListener
                }
                Log.d("tag", "In snapshot listener with ${snapshot?.size()} docs")
                clear()
                snapshot?.documents?.forEach {
                    favorites.add(Star.from(it))
                }
                observer()
            }
        subscriptions[fragName] = subscription
    }

    fun clear() {
        favorites.clear()
    }

    fun favoriteStar(star: Star) {
        favorites.add(star)
        ref.add(star)
    }
    fun curStarToString() : String{
        return getCurStar().toString(settings)
    }
    fun curStarAtPosToString(pos: Int) : String{
        return getStarAt(pos).toString(settings)
    }
    fun getStarAt(pos: Int) = favorites[pos]
    fun getCurStar() = favorites[currentPos]
    fun unfavoriteCurStar(){
        ref.document(getCurStar().id).delete()
        favorites.remove(getCurStar())
        currentPos = 0
    }
    fun updatePos(pos: Int){
        currentPos = pos
    }
    fun size() = favorites.size
}