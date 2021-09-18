package my.com.fashionapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.ClaimHistory
import my.com.fashionappstaff.data.Reward
import my.com.fashionappstaff.data.User

class ClaimViewModel: ViewModel()  {

    private val col= Firebase.firestore.collection("claim")
    private val claim = MutableLiveData<List<ClaimHistory>>()

    init {
        col.addSnapshotListener { snap, _ -> claim.value = snap?.toObjects()  }
    }

    fun get(id : String): ClaimHistory?{
        return claim.value?.find { ch -> ch.claimHistoryID == id }
    }

    fun getAll() = claim

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        claim.value?.forEach{ ch -> delete( ch.claimHistoryID )}
    }

    fun set(ch: ClaimHistory){
        col.document(ch.claimHistoryID).set(ch)
    }

    // Calculate the size of products list
    fun calSize() = claim.value?.size ?: 0

    //-------------------------------------------------------------------
    // Validation

    fun validID(): String {
        var newID: String

        val getLastClaim = claim.value?.lastOrNull()?.claimHistoryID.toString()
        val num: String = getLastClaim.substringAfterLast("CLM10")
        newID = "CLM10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "CLM1010") {
            newID = "CLM1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "CLM10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastClaim = claim.value?.lastOrNull()?.claimHistoryID.toString()
                val num: String = getLastClaim.substringAfterLast("CLM10")
                newID = "CLM10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "CLM10null") {
                    newID = "CLM111"
                    newID
                } else newID
            }
            else -> {
                val getLastClaim = claim.value?.lastOrNull()?.claimHistoryID.toString()
                val num: String = getLastClaim.substringAfterLast("CLM1")
                newID = "CLM1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

}