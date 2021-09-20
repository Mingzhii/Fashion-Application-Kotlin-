package my.com.fashionapp

import android.content.Context
import android.content.SharedPreferences
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



        val preferences1 = getSharedPreferences("checkBo", MODE_PRIVATE)
        val checkbox = preferences1?.getString("remember","")

        if (checkbox == "true") {
            val preferences = getSharedPreferences("email", Context.MODE_PRIVATE)
            val emailLogin = preferences?.getString("emailLogin","")

            if (emailLogin != null) {
                navi(emailLogin)
            }
            vm.getAll()
            nav.navigate(R.id.homeFragment)

        }else {

            val sharedPref = getSharedPreferences("checkBo", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = sharedPref!!.edit()
            editor.putString("remember","false")
            editor.apply()
            val sharedPref1 = getSharedPreferences("email", Context.MODE_PRIVATE)
            val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
            editor1.putString("emailLogin","")
            editor1.apply()
            val email = ""

            navi(email)

            binding.bottomNavigation.selectedItemId = R.id.home


        }




    }

    fun navi(email: String) {

        binding.bottomNavigation.setOnItemSelectedListener {
            if(email == ""){
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                    R.id.nav_shop -> nav.navigate(R.id.action_global_categoryFragment)
                    R.id.nav_profile -> nav.navigate(R.id.action_global_profileFragment2)
                }
            } else {
                when (it.itemId) {
                    R.id.nav_home -> nav.navigate(R.id.action_global_homeFragment)
                    R.id.nav_shop -> nav.navigate(R.id.action_global_categoryFragment)
                    R.id.nav_profile -> nav.navigate(R.id.action_global_loginProfileFragment)
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
        }

        nav.popBackStack()

    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
