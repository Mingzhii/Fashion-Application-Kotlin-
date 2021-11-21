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
import my.com.fashionapp.databinding.FragmentVoucherHistoryBinding
import my.com.fashionapp.util.ViewPageHistoryAdapter

class VoucherHistoryFragment : Fragment() {

    private lateinit var binding: FragmentVoucherHistoryBinding
    private val nav by lazy{ findNavController() }
    private val vmV : VoucherViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVoucherHistoryBinding.inflate(inflater, container, false)

        setupTabs()
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgHistoryBack.setOnClickListener { nav.navigate(R.id.action_voucherHistoryFragment_to_voucherFragment) }


        return binding.root
    }

    private fun setupTabs() {

        val adapter = ViewPageHistoryAdapter(childFragmentManager)

        adapter.addFragment(VoucherInvalidFragment(),"Invalid")
        adapter.addFragment(VoucherUsedFragment(),"Used")
        binding.viewPager.adapter = adapter
        binding.tablayoutHistory.setupWithViewPager(binding.viewPager)


    }

}