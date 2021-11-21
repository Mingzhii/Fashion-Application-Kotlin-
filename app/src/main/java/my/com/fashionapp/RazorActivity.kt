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

//        Checkout.preload(getApplicationContext())

//        val preferences = this.getSharedPreferences("email", Context.MODE_PRIVATE)
//        val emailLogin = preferences?.getString("emailLogin","")
//
//        adapter = PaymentProductAdapter() { holder, cart ->
//
//        }
//
//        binding.rv.adapter = adapter
//
//        binding.rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//
//        vmC.getAll().observe(this){ list->
//            val paymentArray = list.filter { p ->
//                p.cartCheck == "Checked"
//            }
//            adapter.submitList(paymentArray)
//        }
//
//        binding.imgVoucherOpen.setOnClickListener {
//            val intent = Intent(this, VoucherApplyFragment::class.java)
//            startActivity(intent)
//        }
//
//        binding.imgCheckOutBack.setOnClickListener {
//            val intent = Intent(this, CartFragment::class.java)
//            startActivity(intent)
//        }
//
//        updateTotal()


//        payButton.setOnClickListener {
            makePayment()
//        }

    }

//    private fun updateTotal() {
//
//        binding.txtUsername.text = username
//        binding.txtPhoneNumber.text = phonenumber
//        binding.txtDeliveryAddress.text = homeaddress
//
//        val vid = intent.getStringExtra("vID")
//        val vName = intent.getStringExtra("vName")
//        val vValue = intent.getDoubleExtra("vValue", 0.0)
//
//
//        if(vid == ""){
//
//            val shipping = 5.00
//
//            binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
//            binding.txtShipping.text =  "RM " + "%.2f".format(shipping)
//
//            val totalP = totalPrice + shipping
//            val disc = 0.0
//
//            binding.txtDiscount2.text = "RM " + "%.2f".format(disc)
//
//            binding.txtTotalPayment.text = "RM " + "%.2f".format(totalP)
//
//            binding.txtTotalPrice.text = "RM " + "%.2f".format(totalP)
//
//            subtotalPrice = totalP
//        } else {
//
//            if (vValue != 5.0){
//
//                val shipping = 5.00
//
//                binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
//                binding.txtShipping.text =  "RM " + "%.2f".format(shipping)
//
//                val totalP = totalPrice + shipping
//                val disc = totalP * vValue
//
//                val totalAll = totalP - disc
//
//                binding.txtDiscount2.text = "-RM " + "%.2f".format(disc)
//
//                binding.txtTotalPayment.text = "RM " + "%.2f".format(totalAll)
//
//                binding.txtTotalPrice.text = "RM " + "%.2f".format(totalAll)
//
//                binding.txtVouchername.text = vName
//
//                subtotalPrice = totalAll
//
//            } else {
//
//                val shipping = 5.0
//
//                binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
//                binding.txtShipping.text =  "RM " + "%.2f".format(shipping)
//
//                val totalP = totalPrice + shipping
//
//                val disc = 5.0
//
//                val totalAll = totalP - disc
//
//                binding.txtDiscount2.text = "-RM " + "%.2f".format(disc)
//
//                binding.txtTotalPayment.text = "RM " + "%.2f".format(totalAll)
//
//                binding.txtTotalPrice.text = "RM " + "%.2f".format(totalAll)
//
//                binding.txtVouchername.text = vName
//
//                subtotalPrice = totalAll
//            }
//
//        }
//
//    }

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

        val orderid = vmO.validID()
        val o = Order(
            orderId = orderid,
            orderProduct = orderProduct,
            orderProductQuantity = orderProductQuantity,
            orderProductTotalPrice = totalPrice.toString(),
//            orderProductSize = cart.cartProductSize,
            orderShipping = user.homeAddress,
            orderUserName = user.userName,
            orderUserPhone = user.phoneNumber,
            orderPaymentId = payid,
            orderStatus = "Paid",
        )

        vmO.set(o)

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