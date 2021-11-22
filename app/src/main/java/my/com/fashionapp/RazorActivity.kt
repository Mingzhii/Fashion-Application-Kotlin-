package my.com.fashionapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.sun.mail.iap.Argument
import kotlinx.android.synthetic.main.activity_razor.*
import my.com.fashionapp.UI.CartFragment
import my.com.fashionapp.UI.VoucherApplyFragment
import my.com.fashionapp.data.*
import my.com.fashionapp.databinding.ActivityRazorBinding
import my.com.fashionapp.util.PaymentProductAdapter
import my.com.fashionappstaff.data.*
import org.json.JSONObject

class RazorActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityRazorBinding
    private val vmU: UserViewModel by viewModels()
    private val vmC: CartViewModel by viewModels()
    private val vmP: PaymentViewModel by viewModels()
    private val vmO: OrderViewModel by viewModels()
    private val vmVC : VoucherClaimViewModel by viewModels()
    private val vmPro : ProductViewModel by viewModels()
//    private val category by lazy { requireArguments().getString("category", "N/A") }

    private lateinit var adapter: PaymentProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vmU.getAll()
        vmC.getAll()
        vmP.getAll()
        vmPro.getAll()
        vmVC.getAll()

        makePayment()

    }


    private fun makePayment() {
        val co = Checkout()
        val preferences = this.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        co.setKeyID("rzp_test_BP9lANfbhjZGhY")

        try {
            val options = JSONObject()
            options.put("name",username)
            options.put("description","CHAN CO. Payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount", subtotalPrice * 100)//pass amount in currency subunits


            val prefill = JSONObject()
            prefill.put("email",emailLogin)
            prefill.put("contact", phonenumber)

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment Sucsess: "+ p0, Toast.LENGTH_LONG).show()
        uploadData()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Failed: "+ p1, Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun uploadData() {

        val preferences = this.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")


        for(i in 0 until checkOutArray.size) {

            val cartID = checkOutArray[i].orderCartID
            val cartProductQuantity = checkOutArray[i].orderProductQuantity
            val cartProduct = checkOutArray[i].orderProductID

            val cart = vmC.get(cartID)
            val product = vmPro.get(cartProduct)
            val quantity = product?.productQuan.toString().toInt()
            val totalquantity = quantity - cartProductQuantity


            val p = product?.let {

                Product(
                    productId      = it.productId,
                    productName    = it.productName,
                    productDescrip = it.productDescrip,
                    productQuan    = totalquantity,
                    productPrice   = it.productPrice,
                    productCategory= it.productCategory,
                    productPhoto   = it.productPhoto,
                )
            }

            if (p != null) {
                vmPro.set(p)
            }


            val c = cart?.let {
                Cart(

                    cartID = cartID,
                    cartUsername = it.cartUsername,
                    cartProductID = it.cartProductID,
                    cartProductName = it.cartProductName,
                    cartProductQuantity = cartProductQuantity,
                    cartProductPrice = it.cartProductPrice,
                    cartProductSize = it.cartProductSize,
                    cartProductPhoto = it.cartProductPhoto,
                    cartTotalPrice = it.cartTotalPrice,
                    cartStatus = "Done The Payment",
                    cartCheck = "",

                    )
            }

            if (c != null) {
                vmC.set(c)
            }

        }

        val user = emailLogin?.let { vmU.getEmail(it) }
        val point = (totalPrice / 10).toString().toDouble()
        val userpoint = user?.userPoint
        val totalpoint = userpoint?.plus(point)

        val u = user?.let {
            User(

                userId = it.userId,
                email = it.email,
                password = it.password,
                userName = it.userName,
                phoneNumber = it.phoneNumber,
                userPhoto = it.userPhoto,
                homeAddress = it.homeAddress,
                userPoint = totalpoint!!.toDouble(),
                userType = it.userType

            )
        }
        if (u != null) {
            vmU.set(u)
        }

        val payid = vmP.validID()

        val p = Payment(

            paymentID = payid,
            userName = user!!.userName,
            paymentMethod = "Pay By Card",
            totalPrice = totalPrice,
            phoneNo = user.phoneNumber

        )
        vmP.set(p)

        for (i in 0 until checkOutArray.size) {

            val cartProductQuantity = checkOutArray[i].orderProductQuantity.toString()
            val cartProduct = checkOutArray[i].orderProductID
            val cartProductSize = checkOutArray[i].orderProductSize

            val o = Order(
                orderProduct = cartProduct,
                orderProductQuantity = cartProductQuantity,
                orderProductTotalPrice = totalPrice.toString(),
                orderProductSize = cartProductSize,
                orderShipping = user.homeAddress,
                orderUserName = user.userName,
                orderUserPhone = user.phoneNumber,
                orderPaymentId = payid,
                orderStatus = "Paid",
            )
            Firebase.firestore
                .collection("orders")
                .document()
                .set(o)
        }

        val vc = vmVC.get(vourID)

        val v = vc?.let {
            VoucherClaim(
                voucherClaimID = vourID,
                voucherId = it.voucherId,
                claimUser = it.claimUser,
                voucherClaimImg = it.voucherClaimImg,
                voucherClaimName = it.voucherClaimName,
                voucherClaimQuantity = it.voucherClaimQuantity - 1,
                voucherClaimExpiryDate = it.voucherClaimExpiryDate,
                voucherValue = it.voucherValue,
                voucherStatus = "Used",
            )
        }

        if (v != null) {
            vmVC.set(v)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}