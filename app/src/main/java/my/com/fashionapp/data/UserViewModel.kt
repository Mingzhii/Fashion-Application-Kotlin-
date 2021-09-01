package my.com.fashionapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {

    private val col= Firebase.firestore.collection("users")
    private val users = MutableLiveData<List<User>>()
    private val userLiveData = MutableLiveData<User>()
    private var listener: ListenerRegistration? = null


    // Remove snapshot listener when view model is destroyed
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    fun getUserLiveDate(): LiveData<User> {
        return userLiveData
    }

    fun getUser(): User? {
        return userLiveData.value
    }



    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> users.value = snap?.toObjects()  }
    }

    fun get(id : String): User?{
        return users.value?.find { u -> u.userId == id }
    }

    fun getAll() = users

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        users.value?.forEach{ u -> delete(u.userId)}
    }

    fun set(u: User){
        col.document(u.userId).set(u)
    }

    private fun nameExists(name: String): Boolean {
        return users.value?.any{ u -> u.userName == name } ?: false
    }

    private fun phoneExists(phone: Int): Boolean {
        return users.value?.any { u -> u.phoneNumber == phone } ?: false
    }

    //-------------------------------------------------------------------
    // Validation


}