package edu.rosehulman.stargaze.models

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserViewModel : ViewModel(){
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)
    var user: User? = null
    fun getOrMakeUser(observer: () -> Unit){
        ref = Firebase.firestore.collection("users").document(Firebase.auth.uid!!)
        if(user!=null){
            observer()
        }else{
            ref.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
                if(snapshot.exists()){
                    user = snapshot.toObject(User::class.java)
                }else{
                    user = User(email = Firebase.auth.currentUser!!.email!!, name= Firebase.auth.currentUser!!.displayName!!)
                    ref.set(user!!)
                }
                observer()
            }
        }
    }

    fun update(newName: String,newBio: String, newHasCompletedSetup: Boolean){
        if(user!=null){
            with(user!!){
                name = newName
                bio = newBio
                completedSetup = newHasCompletedSetup
                ref.set(this)
            }
        }
    }

    fun hasCompletedSetup() = user?.completedSetup ?: false
}