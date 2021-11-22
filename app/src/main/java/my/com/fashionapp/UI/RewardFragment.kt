package my.com.fashionapp.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import my.com.fashionapp.R
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.RewardViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentRewardBinding
import my.com.fashionapp.util.ProductAdapter
import my.com.fashionapp.util.RewardAdapter
import my.com.fashionapp.util.errorDialog
import my.com.fashionapp.util.generateQRCode
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
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgRewardBack.setOnClickListener { nav.navigate(R.id.loginProfileFragment) }
        binding.imgRewardBag.setOnClickListener { nav.navigate(R.id.rewardClaimFragment) }
        binding.imgQrCodeScanner.setOnClickListener {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }

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
            val arrayReward = list.filter { r ->
                r.rewardQuan != 0
            }
            adapter.submitList(arrayReward)
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    errorDialog("Result not found")
                } else {
                    nav.navigate(R.id.rewardDetailFragment, bundleOf("id" to "REW101"))//Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
            nav.navigate(R.id.rewardDetailFragment, bundleOf("id" to "REW101"))
        }
    }



}