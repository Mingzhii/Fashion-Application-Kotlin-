package my.com.fashionapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        val preferences = getSharedPreferences("email", MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        if (emailLogin != null) {
            navi(emailLogin)
        }

        binding.bottomNavigationDelivery.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_paid -> nav.navigate(R.id.deliveryFragment)
                R.id.nav_ship-> nav.navigate(R.id.deliveryToShipFragment)
                R.id.nav_delivering -> nav.navigate(R.id.deliveryDeliveringFragment)
                R.id.nav_delivered -> nav.navigate(R.id.deliveryDeliveredFragment)
                R.id.nav_completed -> nav.navigate(R.id.deliveryCompletedFragment)
            }
            true
        }
        binding.bottomNavigationDelivery.visibility = View.GONE

    }

    fun navi(email: String) {

        binding.bottomNavigation.setOnItemSelectedListener {
            if(email == ""){
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                    R.id.nav_shop -> nav.navigate(R.id.action_global_categoryFragment)
                    R.id.nav_profile -> nav.navigate(R.id.action_global_profileFragment2)
                    R.id.nav_cart -> nav.navigate(R.id.emptyCartFragment)
                }
            } else {
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                    R.id.nav_shop -> nav.navigate(R.id.action_global_categoryFragment)
                    R.id.nav_profile -> nav.navigate(R.id.action_global_loginProfileFragment)
                    R.id.nav_cart -> nav.navigate(R.id.cartFragment)
                }
            }
            true
        }

    }

    override fun onBackPressed() {

        val num = nav.currentDestination?.label

        when(num){
            "fragment_home"          -> super.finish()
            "fragment_category"      -> super.finish()
            "fragment_profile"       -> super.finish()
            "fragment_login_profile" -> super.finish()
            "fragment_cart"          -> super.finish()
            "fragment_empty_cart"    -> super.finish()
        }
        nav.popBackStack()
    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
