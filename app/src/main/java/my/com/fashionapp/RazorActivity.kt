package my.com.fashionapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_razor.*
import my.com.fashionapp.data.*
import my.com.fashionapp.databinding.ActivityRazorBinding
import my.com.fashionappstaff.data.*
import org.json.JSONObject

class RazorActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityRazorBinding
    private val vmU: UserViewModel by viewModels()
    private val vmC: CartViewModel by viewModels()
    private val vmP: PaymentViewModel by viewModels()
    private val vmO: OrderViewModel by viewModels()
    private val vmPro : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRazorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vmU.getAll()
        vmC.getAll()
        vmP.getAll()
        vmPro.getAll()
        Checkout.preload(getApplicationContext())

        payButton.setOnClickListener {
            makePayment()
        }

    }
    private fun makePayment() {
        val co = Checkout()

        co.setKeyID("rzp_test_BP9lANfbhjZGhY")

        try {
            val options = JSONObject()
            options.put("name","Learning Worldz")
            options.put("description","Learning Worldz Payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount", totalPrice * 100)//pass amount in currency subunits

//            val retryObj =  JSONObject()
//            retryObj.put("enabled", true)
//            retryObj.put("max_count", 4)
//            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email","abcxyz@gmail.com")
            prefill.put("contact","8527834283")

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

        )
        vmP.set(p)

        var orderProduct = ""
        var orderProductQuantity = ""

        for (i in 0 until checkOutArray.size) {

            val cartProductQuantity = checkOutArray[i].orderProductQuantity.toString()
            val cartProduct = checkOutArray[i].orderProductID
            orderProduct += cartProduct + ","
            orderProductQuantity += cartProductQuantity + ","

        }
//        orderProduct
//
//        val divide = ","
//
//        val list = orderProduct.split(divide)
//
//        list.size

        val order = vmO.validID()
        val o = Order(
            orderId = order,
            orderProduct = orderProduct,
            orderProductQuantity = orderProductQuantity,
            orderShipping = user.homeAddress,
            orderPaymentId = payid,
        )

        vmO.set(o)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}