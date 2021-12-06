package my.com.fashionapp.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_razor.*
import my.com.fashionapp.MainActivity
import my.com.fashionapp.R
import my.com.fashionapp.data.*
import my.com.fashionapp.databinding.FragmentRazorPayBinding
import my.com.fashionapp.util.PaymentProductAdapter
import my.com.fashionappstaff.data.*
import org.json.JSONObject

class RazorPayFragment : Fragment() {

    private lateinit var binding: FragmentRazorPayBinding
    private val vmU: UserViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmP: PaymentViewModel by activityViewModels()
    private val vmO: OrderViewModel by activityViewModels()
    private val vmPro : ProductViewModel by activityViewModels()
    private val vmVC : VoucherClaimViewModel by activityViewModels()
    private val nav by lazy{ findNavController() }

    private val vid by lazy { requireArguments().getString("vID", "N/A") }

    private lateinit var adapter: PaymentProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRazorPayBinding.inflate(inflater, container, false)
        vmU.getAll()
        vmC.getAll()
        vmP.getAll()
        vmPro.getAll()
        vmVC.getAll()

//        Checkout.preload(activity?.applicationContext)

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        adapter = PaymentProductAdapter() { holder, cart ->

        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmC.getAll().observe(viewLifecycleOwner) { list ->
            val paymentArray = list.filter { p ->
                p.cartCheck == "Checked" && p.cartUsername == username
            }
            adapter.submitList(paymentArray)
        }

        binding.imgCheckOutBack.setOnClickListener { nav.navigate(R.id.action_razorPayFragment_to_cartFragment) }

        binding.imgVoucherOpen.setOnClickListener {
            nav.navigate(R.id.voucherApplyFragment)
        }

        updateTotal()


        binding.payButton.setOnClickListener {
            nav.navigate(R.id.razorActivity)
        }

        return binding.root
    }

    private fun updateTotal() {

        binding.txtUsername.text = username
        binding.txtPhoneNumber.text = phonenumber
        binding.txtDeliveryAddress.text = homeaddress


        val vc = vmVC.get(vid)

        if(vid == ""){

            val shipping = 5.00

            binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
            binding.txtShipping.text =  "RM " + "%.2f".format(shipping)

            val totalP = totalPrice + shipping
            val disc = 0.0

            binding.txtDiscount.text = "RM " + "%.2f".format(disc)

            binding.txtTotalPayment.text = "RM " + "%.2f".format(totalP)

            binding.txtTotalPrice.text = "RM " + "%.2f".format(totalP)

            subtotalPrice = totalP

        } else {
            vourID = vid

            val vouchername = vc?.voucherClaimName
            val value = vc?.voucherValue

            if (value != 5.0){

                val shipping = 5.00

                binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
                binding.txtShipping.text =  "RM " + "%.2f".format(shipping)

                val totalP = totalPrice + shipping
                val disc = totalP * value!!

                val totalAll = totalP - disc

                binding.txtDiscount.text = "-RM " + "%.2f".format(disc)

                binding.txtTotalPayment.text = "RM " + "%.2f".format(totalAll)

                binding.txtTotalPrice.text = "RM " + "%.2f".format(totalAll)

                binding.txtVouchername.text = vouchername

                subtotalPrice = totalAll

            } else {

                val shipping = 5.0

                binding.txtMerSubtotal.text = "RM "+ "%.2f".format(totalPrice)
                binding.txtShipping.text =  "RM " + "%.2f".format(shipping)

                val totalP = totalPrice + shipping

                val disc = 5.0

                val totalAll = totalP - disc

                binding.txtDiscount.text = "-RM " + "%.2f".format(disc)

                binding.txtTotalPayment.text = "RM " + "%.2f".format(totalAll)

                binding.txtTotalPrice.text = "RM " + "%.2f".format(totalAll)

                binding.txtVouchername.text = vouchername

                subtotalPrice = totalAll
            }

        }

    }

}