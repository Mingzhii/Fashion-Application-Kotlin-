package my.com.fashionapp.UI

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import my.com.fashionapp.MainActivity
import my.com.fashionapp.R
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentLoginProfileBinding
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.data.emailAdress
import my.com.fashionappstaff.data.img
import my.com.fashionappstaff.data.username

class LoginProfileFragment : Fragment() {

    private lateinit var binding: FragmentLoginProfileBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()
    private val email by lazy { requireArguments().getString("email", "N/A") }
    private val id by lazy { requireArguments().getString("id", "N/A")}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLoginProfileBinding.inflate(inflater, container, false)

        // TODO
        getImage()
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE


        // Sign Out Method haven't test yer
        binding.conLayLogout.setOnClickListener { logout() }
        binding.conLayCart.setOnClickListener { nav.navigate(R.id.cartFragment) }
        binding.conLayReward.setOnClickListener { nav.navigate(R.id.rewardFragment) }
        binding.conLayHistory.setOnClickListener { nav.navigate(R.id.paymentHistoryFragment) }

        // Navigation

        return binding.root
    }

    private fun getImage() {
        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        if(emailLogin != "") {
            val u = emailLogin?.let { vm.getUserPhoto2(it) }
            if (u != null) {
                load(u)
            }
        }

    }
    private fun logout():Boolean {
        // Logout -> vm.logout
        img = Blob.fromBytes(ByteArray(0))
        username = ""
        emailAdress = ""

        val sharedPref = activity?.getSharedPreferences("checkBo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString("remember","false")
        editor.apply()
        val sharedPref1 = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
        editor1.putString("emailLogin","")
        editor1.apply()

        val test = requireActivity() as MainActivity
        test.navi(emailAdress)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.selectedItemId = R.id.nav_home

        FirebaseAuth.getInstance().signOut()
        val ctx = requireActivity()
//        vm.logout(ctx)
        nav.navigate(R.id.action_global_homeFragment)

        return true
    }

    private fun load (u: User){

        binding.imgUserPic.setImageBitmap(u.userPhoto.toBitmap())
        binding.txtUsername.text = u.userName
    }


}