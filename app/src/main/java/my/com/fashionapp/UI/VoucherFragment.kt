package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.VoucherViewModel
import my.com.fashionapp.databinding.FragmentVoucherBinding
import my.com.fashionapp.util.ViewPageAdapter


class VoucherFragment : Fragment() {

    private lateinit var binding: FragmentVoucherBinding

    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVoucherBinding.inflate(inflater, container, false)
        setupTabs()
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgVoucherBack.setOnClickListener { nav.navigate(R.id.action_voucherFragment_to_loginProfileFragment) }
        binding.txtVoucherHistory.setOnClickListener { nav.navigate(R.id.voucherHistoryFragment) }
        return binding.root
    }

    private fun setupTabs() {

        val adapter = ViewPageAdapter(childFragmentManager)

        adapter.addFragment(ListVoucherFragment(),"All")
        adapter.addFragment(VoucherBagFragment(),"Bag")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }


}