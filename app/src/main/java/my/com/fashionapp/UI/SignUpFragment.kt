package my.com.fashionapp.UI

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentSignUpBinding
import my.com.fashionapp.util.errorDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        // TODO
        vm.getAll()

        binding.btnRegisterBack.setOnClickListener { nav.navigate(R.id.action_global_profileFragment2) }

        binding.txtLogin.setOnClickListener { nav.navigate(R.id.signInFragment) }

        binding.btnRegister.setOnClickListener {

            val email = binding.edtRegisterEmail.editText?.text.toString().trim()
            val password = binding.edtRegisterPassword.editText?.text.toString().trim()

            val args = bundleOf(
                "email" to email,
                "password" to password,
            )

//            val err = vm.validation(email,password)
//            if (err != ""){
//                errorDialog(err)
//            } else {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult>{ t ->
                        if (t.isSuccessful) {
                            val firebaseUser: FirebaseUser = t.result!!.user!!
                            nav.navigate(R.id.setUpProfileFragment, args)
                        } else{
                            val err = t.exception!!.message.toString()
                            errorDialog(err)
                        }
                    }
                )
            }
//          }
        return binding.root
    }

}