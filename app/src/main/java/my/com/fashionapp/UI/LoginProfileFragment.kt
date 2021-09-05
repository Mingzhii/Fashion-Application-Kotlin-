package my.com.fashionapp.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.R
import my.com.fashionapp.data.User
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentLoginProfileBinding
import my.com.fashionapp.util.toBitmap

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



        // Sign Out Method haven't test yer
        binding.conLayLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // TODO
        }

        // Navigation
        binding.bottomNavigation.selectedItemId = R.id.nav_profile

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> nav.navigate(R.id.homeFragment)
                R.id.nav_like -> nav.navigate(R.id.likeFragment)
                R.id.nav_search -> nav.navigate(R.id.searchFragment)
                R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                R.id.nav_profile -> nav.navigate(R.id.loginProfileFragment)
            }
            true
        }

        return binding.root
    }

    private fun getImage() {
        if(email != null){
            val u = vm.getUserPhoto2(email)
            if (u != null){
                load(u)
            }
        } else if (id != null){
            val u = vm.get(id)
            if (u != null){
                load(u)
            }
        }
    }

    private fun load (u: User){

        binding.imgUserPic.setImageBitmap(u.userPhoto.toBitmap())
        binding.txtUsername.text = u.userName
    }


}