package my.com.fashionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import my.com.fashionapp.UI.HomeFragment
import my.com.fashionapp.UI.LikeFragment
import my.com.fashionapp.UI.SearchFragment
import my.com.fashionapp.UI.ShopFragment
import my.com.fashionapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val homeFragment = HomeFragment()
        val likeFragment = LikeFragment()
        val searchFragment = SearchFragment()
        val shopFragment = ShopFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> setCurrentFragment(homeFragment)
                R.id.nav_like -> setCurrentFragment(likeFragment)
                R.id.nav_search -> setCurrentFragment(searchFragment)
                R.id.nav_shop -> setCurrentFragment(shopFragment)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.body_container,fragment)
            commit()
    }
}
