package my.com.fashionapp.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.ProductViewModel

import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentSetUpProfileBinding
import my.com.fashionapp.util.cropToBlob
import my.com.fashionapp.util.errorDialog
import my.com.fashionappstaff.data.User

class SetUpProfileFragment : Fragment() {

    private lateinit var binding: FragmentSetUpProfileBinding
    private val nav by lazy{ findNavController() }
    private val vm: UserViewModel by activityViewModels()
    private val email by lazy { requireArguments().getString("email", "N/A") }
    private val password by lazy { requireArguments().getString("password", "N/A") }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgUserPic.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSetUpProfileBinding.inflate(inflater, container, false)

        // TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgUserPic.setOnClickListener { selectImage() }
        binding.btnDone.setOnClickListener { submit() }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun submit() {
        //TODO
        var chkID = vm.validID()

        val u = User(
            userId = chkID,
            email = email,
            password = password,
            userName = binding.edtUserName.editText?.text.toString().trim(),
            phoneNumber = binding.edtPhoneNumber.editText?.text.toString(),
            userPhoto = binding.imgUserPic.cropToBlob(300,300),
            homeAddress = binding.edtHomeAddress.editText?.text.toString(),
            userPoint = 0.0,
            userType = "User"
        )

        val err = vm.validate(u)
        if (err != ""){
            errorDialog(err)
            return
        }else{
            vm.set(u)

            nav.navigate(R.id.signInFragment)
        }

    }

}