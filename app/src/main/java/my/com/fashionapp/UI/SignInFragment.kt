package my.com.fashionapp.UI

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.MainActivity
import my.com.fashionapp.R
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentSignInBinding
import my.com.fashionapp.util.errorDialog
import my.com.fashionappstaff.data.emailAdress
import my.com.fashionappstaff.data.username

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        vm.getAll()
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        val preferences = activity?.getSharedPreferences("checkBo", MODE_PRIVATE)
        val checkbox = preferences?.getString("remember","")

        if (checkbox == "true") {
            vm.getAll()
            nav.navigate(R.id.homeFragment)
        }else if(checkbox == "false"){
            super.onCreate(savedInstanceState)
        }

        binding.txtRegister.setOnClickListener { nav.navigate(R.id.signUpFragment) }

        binding.btnLoginBack.setOnClickListener { nav.navigate(R.id.action_global_profileFragment2) }

        binding.btnForgetPassword.setOnClickListener { nav.navigate(R.id.forgetPasswordFragment) }
        // Check Box Remember Me need to do
        binding.chkRememberMe.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                val sharedPref = activity?.getSharedPreferences("checkBo", MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPref!!.edit()
                editor.putString("remember","true")
                editor.apply()
            }
            else if (!compoundButton.isChecked){
                val sharedPref = activity?.getSharedPreferences("checkBo", MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPref!!.edit()
                editor.putString("remember","false")
                editor.apply()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.editText?.text.toString().trim()
            val password = binding.edtLoginPassword.editText?.text.toString().trim()

            if (email == "" && password == "") {
                val err = "Email and Password cannot be empty !"
                errorDialog(err)
            } else {
                login()
            }
        }
        return binding.root
    }

    private fun login(){
        val email = binding.edtLoginEmail.editText?.text.toString().trim()
        val password = binding.edtLoginPassword.editText?.text.toString().trim()

        // Sign In using FirebaseAuth
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                if(t.isSuccessful){
                    emailAdress = email
                    val u = vm.getEmail(emailAdress)
                    if (u != null) {
                        username = u.userName
                        username
                    }
                    if (u?.userType == "User"){
                    val sharedPref = activity?.getSharedPreferences("email", MODE_PRIVATE)
                    val editor : SharedPreferences.Editor = sharedPref!!.edit()
                    editor.putString("emailLogin",email)
                    editor.apply()
                    val test = requireActivity() as MainActivity
                    test.navi(emailAdress)
                    val args = bundleOf(
                        "email" to email
                    )
                        nav.navigate(R.id.homeFragment, args)

                    } else {
                        val err = "You do not have no permission ! "
                        errorDialog(err)
                        binding.edtLoginPassword.editText?.text?.clear()
                    }

                }
                else{
                    // Validation and error
                    val err = t.exception!!.message.toString()
                    errorDialog(err)
                }
            }
    }



}