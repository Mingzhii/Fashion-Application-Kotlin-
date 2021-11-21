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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*
import my.com.fashionapp.R
import my.com.fashionapp.data.CartViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentHomeBinding
import my.com.fashionapp.util.ProductAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy{ findNavController() }
    private val vmU : UserViewModel by activityViewModels()
    private val vm: ProductViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // TODO
        vmU.getAll()
        vmC.getAll()

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        vm.search("")
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })


        adapter = ProductAdapter() { holder, product ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.productDetailFragment, bundleOf("id" to product.productId))
            }

        }

        binding.rv.adapter = adapter
//        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        vm.getResult().observe(viewLifecycleOwner) { list ->

            val productArray = list.filter { p ->
                p.productQuan != 0
            }
            adapter.submitList(productArray)
//            binding.txtItem.text = "${list.size} product(s)"
        }


        return binding.root
    }

}