package my.com.fashionapp.UI

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentPaymentMethodBinding
import org.json.JSONObject

class PaymentMethodFragment : Fragment(), PaymentResultListener {

    private lateinit var binding: FragmentPaymentMethodBinding
    private val nav by lazy{ findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)

        binding.btnPayByCard.setOnClickListener { nav.navigate(R.id.payByCardFragment) }

        Checkout.preload(context?.applicationContext)


        return binding.root
    }

    private fun startPayment(amount: Int) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_BP9lANfbhjZGhY")

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","60000")//pass amount in currency subunits

//            val retryObj = JSONObject()
//            retryObj.put("enabled", true)
//            retryObj.put("max_count", 4)
//            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            checkout.open(activity,options)

        }catch (e: Exception){
            Toast.makeText(this.context,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this.context,"Payment Sucsess: "+ p0,Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this.context,"Payment Failed: "+ p1,Toast.LENGTH_LONG).show()
    }

}