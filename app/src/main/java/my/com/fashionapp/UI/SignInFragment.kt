package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.com.fashionapp.R
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentSignInBinding
import my.com.fashionapp.util.errorDialog

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        // TODO
        binding.txtRegister.setOnClickListener { nav.navigate(R.id.signUpFragment) }

        binding.btnLoginBack.setOnClickListener { nav.navigate(R.id.action_global_profileFragment2) }

        binding.btnForgetPassword.setOnClickListener {
            //TODO
        }
        // Check Box Remember Me need to do

        binding.btnLogin.setOnClickListener {
            val email = binding.edtLoginEmail.editText?.text.toString().trim()
            val password = binding.edtLoginPassword.editText?.text.toString().trim()

            // Sign In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { t ->

                    if(t.isSuccessful){
                        //Navigation
                        val err = "Done"
                        errorDialog(err)

                    }
                    else{
                        // Validation and error
                        val err = t.exception!!.message.toString()
                        errorDialog(err)
                    }
                }
        }

        return binding.root
    }



}