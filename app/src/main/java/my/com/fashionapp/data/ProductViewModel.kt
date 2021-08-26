package my.com.fashionapp.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ProductViewModel {

    private val col= Firebase.firestore.collection("products")
    private val products = MutableLiveData<List<Product>>()

    init {
        col.addSnapshotListener { snap, _ -> products.value = snap?.toObjects()  }
    }

    fun get(id : String): Product?{
        return products.value?.find { p -> p.productId == id }
    }

    fun getAll() = products

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteAll(){
        products.value?.forEach{ p -> delete(p.productId)}
    }

    fun set(p: Product){
        col.document(p.productId).set(p)
    }

    private fun nameExists(name: String): Boolean {
        return products.value?.any{ p -> p.productName == name } ?: false
    }

}