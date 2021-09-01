package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentLikeBinding


class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private val nav by lazy{ findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLikeBinding.inflate(inflater, container, false)

        // TODO
        binding.bottomNavigation.selectedItemId = R.id.nav_like

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                R.id.nav_like -> nav.navigate(R.id.likeFragment)
                R.id.nav_search -> nav.navigate(R.id.searchFragment)
                R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                R.id.nav_profile -> nav.navigate(R.id.signUpFragment)
            }
            true
        }

        return binding.root
    }

}