package my.com.fashionapp.UI

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.VoucherClaimViewModel
import my.com.fashionapp.data.VoucherViewModel
import my.com.fashionapp.databinding.FragmentVoucherUsedBinding
import my.com.fashionapp.util.VoucherAdapter
import my.com.fashionapp.util.VoucherClaimAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VoucherUsedFragment : Fragment() {

    private lateinit var binding : FragmentVoucherUsedBinding
    private val nav by lazy{ findNavController() }
    private val vmVC : VoucherClaimViewModel by activityViewModels()

    private lateinit var adapter: VoucherClaimAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentVoucherUsedBinding.inflate(inflater, container, false)

        vmVC.getAll()

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE



        adapter = VoucherClaimAdapter() { holder, voucher ->
            // Item click
            holder.imgExpiry.visibility = View.GONE
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

        vmVC.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                v.voucherStatus == "Used"
            }
            adapter.submitList(voucherArray)
        }


        return binding.root
    }


}