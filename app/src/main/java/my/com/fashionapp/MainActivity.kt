package my.com.fashionapp

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.UI.*
import my.com.fashionapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> nav.navigate(R.id.homeFragment)
                R.id.nav_like -> nav.navigate(R.id.likeFragment)
                R.id.nav_search -> nav.navigate(R.id.searchFragment)
                R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                R.id.nav_profile -> nav.navigate(R.id.profileFragment)
            }
            true
        }

        // TODO
//        val homeFragment = HomeFragment()
//        val likeFragment = LikeFragment()
//        val searchFragment = SearchFragment()
//        val shopFragment = insertProductFragment()
//        val profileFragment = SignUpFragment()
//
//        setCurrentFragment(homeFragment)

//        binding.bottomNavigation.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.nav_home -> nav.navigate(R.id.homeFragment)
//                R.id.nav_like -> setCurrentFragment(likeFragment)
//                R.id.nav_search -> setCurrentFragment(searchFragment)
//                R.id.nav_shop -> setCurrentFragment(shopFragment)
//                R.id.nav_profile -> nav.navigate(R.id.signUpFragment)
//
//            }
//            true
//        }

    }

//    private fun setCurrentFragment(fragment: Fragment)=
//        supportFragmentManager.beginTransaction().apply{
//            replace(R.id.body_container,fragment)
//            commit()
//    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
