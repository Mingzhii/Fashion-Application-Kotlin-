package my.com.fashionapp.UI

import android.graphics.Color
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
import my.com.fashionapp.databinding.FragmentVoucherInvalidBinding
import my.com.fashionapp.util.VoucherAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VoucherInvalidFragment : Fragment() {

    private lateinit var binding : FragmentVoucherInvalidBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()

    private lateinit var adapter: VoucherAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentVoucherInvalidBinding.inflate(inflater, container, false)

        vmV.getAll()

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE



        adapter = VoucherAdapter() { holder, voucher ->
            // Item click

            holder.cv.setBackgroundColor(Color.rgb(177,175,175))
            holder.root.isEnabled = false
//            holder.root.setBackgroundColor(Color.rgb(177,175,175))
            holder.btnClaim.visibility = View.GONE
        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)

        vmV.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                v.voucherExpiryDate < formatDate || v.voucherQuantity == 0
            }
            adapter.submitList(voucherArray)
        }



        return binding.root
    }


}