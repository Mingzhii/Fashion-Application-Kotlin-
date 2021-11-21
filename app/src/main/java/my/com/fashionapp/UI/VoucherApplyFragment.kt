package my.com.fashionapp.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.fashionapp.R
import my.com.fashionapp.RazorActivity
import my.com.fashionapp.data.VoucherClaimViewModel
import my.com.fashionapp.databinding.FragmentVoucherApplyBinding
import my.com.fashionapp.util.VoucherClaimAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VoucherApplyFragment : DialogFragment() {

    private lateinit var binding: FragmentVoucherApplyBinding
    private val nav by lazy{ findNavController() }
    private val vmVC : VoucherClaimViewModel by activityViewModels()

    private lateinit var adapter: VoucherClaimAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentVoucherApplyBinding.inflate(inflater, container, false)

        adapter = VoucherClaimAdapter() { holder, voucherCliam ->
            // Item click

            holder.btnClaim.text = "Use"
            holder.imgExpiry.visibility = View.GONE
            holder.txtTerm.visibility = View.GONE

            //Claim Voucher
            holder.btnClaim.setOnClickListener {

                nav.navigate(R.id.razorPayFragment, bundleOf("vID" to voucherCliam.voucherClaimID))
            }

        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)

        vmVC.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                v.voucherClaimQuantity != 0 && v.voucherClaimExpiryDate > formatDate
            }

            adapter.submitList(voucherArray)
        }

        return binding.root
    }

}