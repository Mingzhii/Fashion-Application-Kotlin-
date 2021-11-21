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
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.databinding.FragmentCategoryDetailBinding
import my.com.fashionapp.util.ProductAdapter


class CategoryDetailFragment : Fragment() {

    private lateinit var binding : FragmentCategoryDetailBinding
    private val nav by lazy{ findNavController() }
    private val vm: ProductViewModel by activityViewModels()
    private val category by lazy { requireArguments().getString("category", "N/A") }

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)

        vm.search("")

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgBack.setOnClickListener { nav.navigate(R.id.action_categoryDetailFragment_to_categoryFragment2) }

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

        vm.getResult().observe(viewLifecycleOwner) { list ->

            val productArray = list.filter { p ->
                p.productCategory == category && p.productQuan != 0
            }
            adapter.submitList(productArray)
//            binding.txtItem.text = "${list.size} product(s)"

            if(category == "Men"){
                binding.textView34.setText("Men");
            }else{
                binding.textView34.setText("Women");
            }
        }

        return binding.root
    }


}