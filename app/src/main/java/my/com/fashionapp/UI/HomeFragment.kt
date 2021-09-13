package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.databinding.FragmentHomeBinding
import my.com.fashionapp.util.ProductAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy{ findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE

        adapter = ProductAdapter() { holder, product ->
            // Item click
            holder.root.setOnClickListener {
               nav.navigate(R.id.productDetailFragment, bundleOf("id" to product.productId))
            }
            // Delete button click
//            holder.btnDelete.setOnClickListener { delete(product.productId) }
        }

        binding.rv.layoutManager = GridLayoutManager(context, 2)
        binding.rv.adapter = adapter
//        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
//            binding.txtItem.text = "${list.size} product(s)"
        }


        return binding.root
    }

}