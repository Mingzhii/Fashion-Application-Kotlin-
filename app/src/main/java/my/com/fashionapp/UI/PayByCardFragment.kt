package my.com.fashionapp.UI

import android.R.attr
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.R
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentPayByCardBinding
import my.com.fashionapp.util.*
import java.lang.StringBuilder
import java.text.DecimalFormat
import android.R.attr.divider

import android.R.attr.digits
import android.content.Context
import androidx.core.view.isEmpty
import com.google.firebase.firestore.Blob
import my.com.fashionapp.data.CartViewModel
import my.com.fashionapp.data.PaymentViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.data.*
import java.util.*


class PayByCardFragment : Fragment() {

    private lateinit var binding: FragmentPayByCardBinding
    private val nav by lazy{ findNavController() }
    private val vmU: UserViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmP: PaymentViewModel by activityViewModels()
    private val vmPro :ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPayByCardBinding.inflate(inflater, container, false)

        // TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgPayByCardBack.setOnClickListener { nav.navigateUp() }

        val n  = (0..999999).random()
        val fmt = DecimalFormat("000000")
        val OTP = fmt.format(n)

        binding.txtTotalPrice.text = "%.2f".format(totalPrice)

        val point = totalPrice / 10
        binding.txtPointEarn.text = "%.2f".format(point)

        binding.txtCardNumber.editText?.requestFocus()
        binding.txtOTPCode.isEnabled = false

        binding.txtCardNumber.editText?.addTextChangedListener(object : TextWatcher {
            private val space = ' '
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                // Remove all spacing char
                var pos = 0
                while (true) {
                    if (pos >= s.length) break
                    if (space == s[pos] && ((pos + 1) % 5 != 0 || pos + 1 == s.length)) {
                        s.delete(pos, pos + 1)
                    } else {
                        pos++
                    }
                }

                // Insert char where needed.
                pos = 4
                while (true) {
                    if (pos >= s.length) break
                    val c = s[pos]
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space)
                    }
                    pos += 5
                }
            }
        })

        binding.txtCardExpiryDate.editText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {

                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    binding.txtCardExpiryDate.editText?.setText(p0.toString() + "/")

                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {

                    binding.txtCardExpiryDate.editText?.setText(p0.toString().replace("/", ""))
                }
            }
        })

        binding.btnPay.setOnClickListener { pay(OTP) }
        binding.btnOTP.setOnClickListener { sendOTP(OTP) }

        return binding.root
    }

    private fun pay(OTP: String) {

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        val uId = u?.userId

        val otp = binding.txtOTPCode.editText?.text.toString()

        if (otp.isEmpty() || binding.txtCardExpiryDate.isEmpty() || binding.txtCardName.isEmpty() || binding.txtCvv.isEmpty()){

            snackbar("Some Detail Is Empty, Please Fill Up Carefully. ")

        } else {

            if (otp == OTP){

                for(i in 0 until checkOutArray.size) {

                    val cartID = checkOutArray[i].orderCartID
                    val cartProductQuantity = checkOutArray[i].orderProductQuantity
                    val cartProduct = checkOutArray[i].orderProductID

                    val product = vmPro.get(cartProduct)
                    val quantity = product?.productQuan.toString().toInt()
                    val totalquantity = quantity - cartProductQuantity
                    val cart = vmC.get(cartID)

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
                            cartStatus = "Done The Payment",
                        )
                    }

                    if (c != null) {
                        vmC.set(c)
                    }

                }

                val user = emailLogin?.let { vmU.getEmail(it) }
                val point = binding.txtPointEarn.text.toString().toDouble()
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
                    userName = username,
                    paymentMethod = "Pay By Card",
                    totalPrice = binding.txtTotalPrice.text.toString().toDouble(),
                )
                vmP.set(p)

                nav.navigate(R.id.action_global_homeFragment)

            } else {

                binding.txtOTPCode.editText?.requestFocus()

                snackbar("Wrong OTP Code")
            }
        }
    }

    private fun sendOTP(OTP: String) {

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        if(binding.txtCardNumber.editText?.text.isNullOrEmpty()){
            errorDialog("Card Number Cannot Be Empty, Please Fill In")
        }
        else{

            binding.txtOTPCode.isEnabled = true

            val email = emailLogin

            if (email != null) {
                send(email, OTP)
            }

        }
    }

    private fun send(email: String, OTP: String) {

        hideKeyboard()


        val subject = "Your OTP Code"
        val content = """
            <p>Your <b>OTP<b> is:</p>
            <h1 style="color: Blue">$OTP</h1>
            <p>Thank you very much.</p>
        """.trimIndent()

        SimpleEmail()
            .to(email)
            .subject(subject)
            .content(content)
            .isHtml()
            .send() {
                snackbar("Sent")
                binding.btnOTP.isEnabled = true
            }

        snackbar("Sending...")
        binding.btnOTP.isEnabled = false

    }
    //
    private fun isEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }


}