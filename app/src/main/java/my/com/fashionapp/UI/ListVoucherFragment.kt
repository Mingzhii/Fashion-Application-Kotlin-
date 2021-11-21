package my.com.fashionapp.UI

import android.content.Context
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
import my.com.fashionapp.databinding.FragmentListVoucherBinding
import my.com.fashionapp.util.VoucherAdapter
import my.com.fashionappstaff.data.Voucher
import my.com.fashionappstaff.data.VoucherClaim
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListVoucherFragment : Fragment() {

    private lateinit var binding : FragmentListVoucherBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()
    private val vmVC : VoucherClaimViewModel by activityViewModels()
    private val vmU  : UserViewModel by activityViewModels()

    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentListVoucherBinding.inflate(inflater,container, false)

        vmV.getAll()
        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val user = emailLogin?.let { vmU.getEmail(it) }


        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        adapter = VoucherAdapter() { holder, voucher ->
            // Item click

            val id1 = vmVC.getitem(voucher.voucherId)

            if (id1 != null) {
                if(id1.voucherId == voucher.voucherId){
                    holder.btnClaim.text = "Use"
                    holder.btnClaim.isEnabled = false
                }
            }

            holder.imgExpiry.visibility = View.GONE
            holder.txtTerm.setOnClickListener {
                nav.navigate(R.id.termNConditionFragment, bundleOf("id" to voucher.voucherId))
            }

            //Claim Voucher
            holder.btnClaim.setOnClickListener {


                val vcId = vmVC.validID()

                val vc = VoucherClaim(
                    voucherClaimID = vcId,
                    voucherId = voucher.voucherId,
                    claimUser = user!!.userName,
                    voucherClaimImg = voucher.voucherImg,
                    voucherClaimName = voucher.voucherName,
                    voucherClaimQuantity = 1,
                    voucherClaimExpiryDate = voucher.voucherExpiryDate,
                    voucherValue = voucher.voucherValue,
                    voucherStatus = "Claim"
                )
                vmVC.set(vc)
                val v = Voucher(
                    voucherId = voucher.voucherId,
                    voucherImg = voucher.voucherImg,
                    voucherName = voucher.voucherName,
                    voucherTerm = voucher.voucherTerm,
                    voucherDescription = voucher.voucherDescription,
                    voucherQuantity = voucher.voucherQuantity - 1,
                    voucherDate = voucher.voucherDate,
                    voucherExpiryDate = voucher.voucherExpiryDate,
                    voucherValue = voucher.voucherValue,
                )
                vmV.set(v)

                holder.btnClaim.text = "USE"
                holder.btnClaim.isEnabled = false
            }

        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val currentDate = LocalDate.now()
        val viewFormatter = DateTimeFormatter.ofPattern("dd MMM, YYYY")
        val formatDate = viewFormatter.format(currentDate)

        vmV.getResult().observe(viewLifecycleOwner) { list ->

            val voucherArray = list.filter { v ->
                v.voucherQuantity != 0 && v.voucherExpiryDate > formatDate
            }

            adapter.submitList(voucherArray)
        }

        return binding.root
    }

}