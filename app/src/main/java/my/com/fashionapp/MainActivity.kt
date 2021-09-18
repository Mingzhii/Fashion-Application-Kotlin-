package my.com.fashionapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.ActivityMainBinding
import my.com.fashionappstaff.data.emailAdress

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val vm: UserViewModel by viewModels()
    private lateinit var abc: AppBarConfiguration
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")


        if (emailLogin != null) {
            navi(emailLogin)
        }
    }

    fun navi(email: String) {

        binding.bottomNavigation.setOnItemSelectedListener {
            if(email == ""){
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.homeFragment)
                    R.id.nav_like -> nav.navigate(R.id.likeFragment)
                    R.id.nav_search -> nav.navigate(R.id.searchFragment)
                    R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                    R.id.nav_profile -> nav.navigate(R.id.profileFragment)
                }
            } else {
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.homeFragment)
                    R.id.nav_like -> nav.navigate(R.id.likeFragment)
                    R.id.nav_search -> nav.navigate(R.id.searchFragment)
                    R.id.nav_shop -> nav.navigate(R.id.shopFragment)
                    R.id.nav_profile -> nav.navigate(R.id.loginProfileFragment)
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
