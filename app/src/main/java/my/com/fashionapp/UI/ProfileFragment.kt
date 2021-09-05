package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.R
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // TODO

        binding.btnRegisterProfile2.setOnClickListener { nav.navigate(R.id.signUpFragment) }

        binding.btnLoginProfile2.setOnClickListener { nav.navigate(R.id.signInFragment) }

        binding.bottomNavigation.selectedItemId = R.id.nav_profile

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                R.id.nav_like -> nav.navigate(R.id.likeFragment)
                R.id.nav_search -> nav.navigate(R.id.searchFragment)
                R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                R.id.nav_profile -> nav.navigate(R.id.profileFragment)
            }
            true
        }



        return binding.root
    }

}
