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
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.ClaimViewModel
import my.com.fashionapp.data.RewardViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentRewardClaimBinding
import my.com.fashionapp.util.RewardAdapter
import my.com.fashionapp.util.RewardHistoryAdapter

class RewardClaimFragment : Fragment() {

    private lateinit var binding: FragmentRewardClaimBinding
    private val nav by lazy{ findNavController() }
    private val vmU: UserViewModel by activityViewModels()
    private val vmC: ClaimViewModel by activityViewModels()

    private lateinit var adapter: RewardHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRewardClaimBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgRewardBack2.setOnClickListener { nav.navigate(R.id.rewardFragment) }

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        val username = u?.userName

        adapter = RewardHistoryAdapter() { holder, ClaimHistory ->
            // Item click
            holder.root.setOnClickListener {
//                nav.navigate(R.id.rewardDetailFragment, bundleOf("id" to reward.rewardID))
            }
//             Delete button click
//            holder.btnDelete.setOnClickListener { delete(product.productId) }
        }

        binding.rv.adapter = adapter

        vmC.getAll().observe(viewLifecycleOwner) { list ->

            val arrayHistory = list.filter { c ->
                c.username == username
            }
            adapter.submitList(arrayHistory)
        }


        return binding.root
    }

}