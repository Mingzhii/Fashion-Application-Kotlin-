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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.data.VoucherClaimViewModel
import my.com.fashionapp.data.VoucherViewModel
import my.com.fashionapp.databinding.FragmentVoucherBagBinding
import my.com.fashionapp.util.VoucherAdapter
import my.com.fashionapp.util.VoucherClaimAdapter
import my.com.fashionappstaff.data.Voucher
import my.com.fashionappstaff.data.VoucherClaim
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VoucherBagFragment : Fragment() {

    private lateinit var binding: FragmentVoucherBagBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()
    private val vmVC : VoucherClaimViewModel by activityViewModels()
    private val vmU  : UserViewModel by activityViewModels()

    private lateinit var adapter: VoucherClaimAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVoucherBagBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        adapter = VoucherClaimAdapter() { holder, voucherCliam ->
            // Item click

            holder.btnClaim.text = "Use"
            holder.imgExpiry.visibility = View.GONE
            holder.txtTerm.setOnClickListener {
                nav.navigate(R.id.termNConditionFragment, bundleOf("id" to voucherCliam.voucherId))
            }

            //Claim Voucher
            holder.btnClaim.setOnClickListener {

            }

        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)

        vmVC.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                 v.voucherClaimQuantity != 0 && v.voucherClaimExpiryDate > formatDate && v.voucherStatus != "Used"
            }

            adapter.submitList(voucherArray)
        }




        return binding.root
    }


}