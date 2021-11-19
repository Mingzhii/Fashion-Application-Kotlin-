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
import my.com.fashionapp.data.CartViewModel
import my.com.fashionapp.data.OrderViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentDeliveryDeliveringBinding
import my.com.fashionapp.util.DeliveryAdapter
import my.com.fashionapp.util.toBitmap

class DeliveryDeliveringFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryDeliveringBinding
    private val nav by lazy{ findNavController() }
    private val vmU : UserViewModel by activityViewModels()
    private val vm: ProductViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmO: OrderViewModel by activityViewModels()

    private lateinit var adapter: DeliveryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryDeliveringBinding.inflate(inflater, container, false)

        vmO.getAll()
        vm.getAll()

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })

        binding.imgDeliveryBack.setOnClickListener { nav.navigate(R.id.action_deliveryFragment_to_loginProfileFragment) }

        adapter = DeliveryAdapter() { holder, product ->

            val p = vm.get(product.orderProduct)

            if (p != null) {
                holder.imgPhoto.setImageBitmap(p.productPhoto.toBitmap())
                holder.txtProductName.setText(p.productName)
            }

            holder.root.setOnClickListener {
                nav.navigate(R.id.productDetailFragment, bundleOf("id" to product.orderProduct))
            }
        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmO.getAll().observe(viewLifecycleOwner){list ->
            adapter.submitList(list)
        }


        binding.bottomNavigationDelivery.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_paid -> nav.navigate(R.id.deliveryFragment)
                R.id.nav_ship-> nav.navigate(R.id.deliveryToShipFragment)
                R.id.nav_delivering -> nav.navigate(R.id.deliveryDeliveringFragment)
                R.id.nav_delivered -> nav.navigate(R.id.deliveryDeliveredFragment)
                R.id.nav_completed -> nav.navigate(R.id.deliveryCompletedFragment)
            }
            true
        }

        return binding.root
    }


}