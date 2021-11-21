package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.R
import my.com.fashionapp.data.VoucherViewModel
import my.com.fashionapp.databinding.FragmentTermNConditionBinding
import my.com.fashionapp.util.ViewPageAdapter
import my.com.fashionapp.util.toBitmap

class TermNConditionFragment : Fragment() {

    private lateinit var binding : FragmentTermNConditionBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id", "N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTermNConditionBinding.inflate(inflater, container, false)

        binding.imgVoucherBack2.setOnClickListener {
            nav.navigate(R.id.action_termNConditionFragment_to_voucherFragment2)
        }

        binding.btnOk.setOnClickListener{
            nav.navigate(R.id.action_termNConditionFragment_to_voucherFragment2)
        }

        detail()


        return binding.root
    }

    private fun detail() {

        val v = vmV.get(id)
        if (v == null){
            nav.navigateUp()
            return
        }

        binding.imgVoucher2.setImageBitmap(v.voucherImg.toBitmap())
        binding.txtVoucherName.setText(v.voucherName)
        binding.txtValidDate.setText(v.voucherExpiryDate)
        binding.txtValidTime.text = v.voucherDate + " " + v.voucherExpiryDate
        binding.txtCondition.setText(v.voucherDescription)
        binding.txtTerm.setText(v.voucherTerm)


    }

}