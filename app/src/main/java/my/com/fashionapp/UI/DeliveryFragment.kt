package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.*
import my.com.fashionapp.databinding.FragmentDeliveryBinding
import my.com.fashionapp.util.DeliveryAdapter
import my.com.fashionapp.util.toBitmap

class DeliveryFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryBinding
    private val nav by lazy{ findNavController() }
    private val vmU : UserViewModel by activityViewModels()
    private val vm: ProductViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmO: OrderViewModel by activityViewModels()
    private val vmP: PaymentViewModel by activityViewModels()

    private lateinit var adapter: DeliveryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        vmO.getAll()
        vm.getAll()

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.VISIBLE


        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })

        binding.imgDeliveryBack.setOnClickListener { nav.navigate(R.id.deliveryFragment_to_loginProfileFragment) }

        adapter = DeliveryAdapter() { holder, product ->

            val p = vm.get(product.orderProduct)
            val o = vmO.get(product.orderId)
            val pay = vmP.get(product.orderPaymentId)

            if (p != null) {
                holder.imgPhoto.setImageBitmap(p.productPhoto.toBitmap())
                holder.txtProductName.setText(p.productName)
            }

            holder.root.setOnClickListener {
                if (o != null && p != null) {
                    nav.navigate(R.id.orderDetailFragment, bundleOf("id" to product.orderId, "id1" to product.orderProduct,"id2" to product.orderPaymentId))
                }
            }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmO.getAll().observe(viewLifecycleOwner){list ->
            var orderArray = list.filter { o ->
                o.orderStatus == "Paid"
            }
            adapter.submitList(orderArray)
        }

        return binding.root
    }


}