package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import my.com.fashionapp.R
import my.com.fashionapp.data.OrderViewModel
import my.com.fashionapp.data.PaymentViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentOrderDetailBinding
import my.com.fashionapp.databinding.FragmentUpdateToCompletedDeliveryBinding
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.Order


class UpdateToCompletedDeliveryFragment : Fragment() {

    private lateinit var binding: FragmentUpdateToCompletedDeliveryBinding
    private val vmU : UserViewModel by activityViewModels()
    private val vm: ProductViewModel by activityViewModels()
    private val vmO: OrderViewModel by activityViewModels()
    private val vmP: PaymentViewModel by activityViewModels()
    private val nav by lazy{ findNavController() }

    private val id by lazy { requireArguments().getString("id", "N/A") }
    private val id1 by lazy { requireArguments().getString("id1", "N/A") }
    private val id2 by lazy { requireArguments().getString("id2", "N/A") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateToCompletedDeliveryBinding.inflate(inflater, container, false)

//        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
//        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        orderDetail()
        binding.imgOrderDetailsBack.setOnClickListener { nav.navigate(R.id.deliveryFragment) }

        binding.btnOrderReceived.setOnClickListener { updateOrder() }

        val p1 = vm.get(id1)

        binding.imageView8.setOnClickListener {
            if (p1 != null) {
                nav.navigate(R.id.deliveryProductDetailFragment, bundleOf("id" to id1))
            }else{
                snackbar("Fail")
            }
        }

        return binding.root
    }

    private fun updateOrder() {
        val order = vmO.get(id)

        val o = order?.let {
            Order(
                orderId = id,
                orderPaymentId = id2,
                orderProduct = id1,
                orderProductQuantity = it.orderProductQuantity,
                orderProductTotalPrice = it.orderProductTotalPrice,
                orderShipping = it.orderShipping,
                orderUserName = it.orderUserName,
                orderUserPhone = it.orderUserPhone,
                orderStatus = "Completed"
            )
        }
        if (o != null) {
            vmO.set(o)
        }else{
            snackbar("Update Fail")
        }
        nav.navigate(R.id.deliveryFragment)
    }

    private fun orderDetail() {

        val o   = vmO.get(id)
        val p   = vm.get(id1)
        val pay = vmP.get(id2)

        if(o != null){
            binding.txtShippingInfo.setText(o.orderId)
            binding.txtOrderDate.setText(o.orderDate)
            binding.txtAddress.setText(o.orderShipping)
            binding.txtOrderDetailsPrice.setText("RM" + o.orderProductTotalPrice)
            binding.txtOrderDetailsQuant.setText("X" + o.orderProductQuantity)
            binding.txtRecipientName.setText(o.orderUserName)
            binding.txtPhoneNo.setText(o.orderUserPhone)
            binding.txtOrderDetailStatus.setText(o.orderStatus)
        }
        if(p != null){
            binding.txtOrderDetailsName.setText(p.productName)
            binding.imageView8.setImageBitmap(p.productPhoto.toBitmap())
        }
        if(pay != null) {
            binding.txtPaymentMethod.setText(pay.paymentMethod).toString()
        }
    }
    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }
}