package my.com.fashionapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.fashionappstaff.data.VoucherClaim

class VoucherClaimViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("voucherClaim")
    private val voucherClaim = MutableLiveData<List<VoucherClaim>>()
    private var searchVoucher = listOf<VoucherClaim>()
    private val result = MutableLiveData<List<VoucherClaim>>()
    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { snap, _ -> voucherClaim.value = snap?.toObjects()
            searchVoucher = snap!!.toObjects<VoucherClaim>()
            updateResult() }
    }

    // Search
    fun search(name: String){
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = searchVoucher

        //Search
        list = list.filter {
            it.voucherClaimName.contains(name, true)
        }

        result.value = list
    }

    fun getResult() = result

    fun get(id : String): VoucherClaim?{
        return voucherClaim.value?.find { v -> v.voucherClaimID == id }
    }

    fun getitem(id: String): VoucherClaim?{
        return voucherClaim.value?.find { v -> v.voucherId == id }
    }

    fun getAll() = voucherClaim

    fun delete(id : String){
        col.document(id).delete()
    }

    fun set(v: VoucherClaim){
        col.document(v.voucherClaimID).set(v)
    }

    // Calculate the size of products list
    fun calSize() = voucherClaim.value?.size ?: 0

    fun validID(): String {
        var newID: String

        val getLastReward = voucherClaim.value?.lastOrNull()?.voucherClaimID.toString()
        val num: String = getLastReward.substringAfterLast("VClAIM10")
        newID = "VClAIM10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "VClAIM1010") {
            newID = "VClAIM1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "VClAIM10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastReward = voucherClaim.value?.lastOrNull()?.voucherClaimID.toString()
                val num: String = getLastReward.substringAfterLast("VClAIM10")
                newID = "VClAIM10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "VClAIM10null") {
                    newID = "VClAIM111"
                    newID
                } else newID
            }
            else -> {
                val getLastReward = voucherClaim.value?.lastOrNull()?.voucherClaimID.toString()
                val num: String = getLastReward.substringAfterLast("VClAIM1")
                newID = "VClAIM1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }


}