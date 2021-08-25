package my.com.fashionapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class User (

    @DocumentId
    var userId      : String = "",
    var userName    : String = "",
    var uesrAge     : Int = 0,
    var phoneNumber : Int = 0,
    var userPhoto   : Blob = Blob.fromBytes(ByteArray(0)),
    var address     : String = "",
)