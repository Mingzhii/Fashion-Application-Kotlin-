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
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.VoucherViewModel
import my.com.fashionapp.databinding.FragmentVoucherBinding
import my.com.fashionapp.util.ProductAdapter
import my.com.fashionapp.util.VoucherAdapter


class VoucherFragment : Fragment() {

    private lateinit var binding: FragmentVoucherBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()

    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVoucherBinding.inflate(inflater, container, false)

        //TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        adapter = VoucherAdapter() { holder, voucher ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.productDetailFragment, bundleOf("id" to voucher.voucherId))
            }

        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmV.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                v.voucherQuantity != 0
            }
            adapter.submitList(voucherArray)
//            binding.txtItem.text = "${list.size} product(s)"
        }


        return binding.root
    }


}