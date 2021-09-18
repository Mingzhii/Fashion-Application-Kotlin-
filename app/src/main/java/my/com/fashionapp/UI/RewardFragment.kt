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
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.RewardViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentRewardBinding
import my.com.fashionapp.util.ProductAdapter
import my.com.fashionapp.util.RewardAdapter
import my.com.fashionappstaff.data.emailAdress


class RewardFragment : Fragment() {

    private lateinit var binding: FragmentRewardBinding
    private val nav by lazy{ findNavController() }
    private val vmU: UserViewModel by activityViewModels()
    private val vmR: RewardViewModel by activityViewModels()

    private lateinit var adapter: RewardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRewardBinding.inflate(inflater, container, false)

        // TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        binding.imgRewardBack.setOnClickListener { nav.navigate(R.id.loginProfileFragment) }

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        if (u != null) {
            binding.txtUserPoint.setText(u.userPoint.toString())
        }

        adapter = RewardAdapter() { holder, reward ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.rewardDetailFragment, bundleOf("id" to reward.rewardID))
            }
//             Delete button click
//            holder.btnDelete.setOnClickListener { delete(product.productId) }
        }

        binding.rv.adapter = adapter

        vmR.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }




        return binding.root
    }



}