package my.com.fashionapp.UI

import android.app.Activity
import android.content.Context
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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentUpdateUserProfileBinding
import my.com.fashionapp.util.cropToBlob
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.User
import my.com.fashionappstaff.data.emailAdress
import my.com.fashionappstaff.data.img
import my.com.fashionappstaff.data.username

class UpdateUserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUpdateUserProfileBinding
    private val nav by lazy { findNavController() }
    private val vm: UserViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgProfilePic.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentUpdateUserProfileBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val email = emailLogin

        val u = email?.let { vm.getUserPhoto2(it) }
        if (u != null) {
            load(u)
        }

        binding.edtHomeAdress.isEnabled = false
        binding.edtPhoneNumber.isEnabled = false
        binding.edtUsername.isEnabled = false
        binding.btnUpdateUserProfileDone.visibility = View.GONE

        binding.imgEdt.setOnClickListener {
            binding.edtHomeAdress.isEnabled = true
            binding.edtPhoneNumber.isEnabled = true
            binding.edtUsername.isEnabled = true
            binding.btnUpdateUserProfileDone.visibility = View.VISIBLE
        }

        binding.imgUpdateUserProfileBack.setOnClickListener { nav.navigate(R.id.action_updateUserProfileFragment_to_loginProfileFragment) }
        binding.btnUpdateUserProfileDone.setOnClickListener {
            if(u != null){
                val u = User(
                    userId = u.userId,
                    email = u.email,
                    password = u.password,
                    userName = binding.edtUsername.editText?.text.toString(),
                    phoneNumber = binding.edtPhoneNumber.editText?.text.toString(),
                    userPhoto = binding.imgProfilePic.cropToBlob(300,300),
                    homeAddress = binding.edtHomeAdress.editText?.text.toString(),
                    userType = u.userType,
                    userPoint = u.userPoint
                )
                username = binding.edtUsername.editText?.text.toString()
                img = binding.imgProfilePic.cropToBlob(300,300)

                vm.set(u)
                nav.navigate(R.id.action_updateUserProfileFragment_to_loginProfileFragment)

            }

        }

        binding.imgProfilePic.setOnClickListener { select() }

        return binding.root
    }
    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun load(u: User) {

        binding.imgProfilePic.setImageBitmap(u.userPhoto.toBitmap())
        binding.edtUsername.editText?.setText(u.userName)
        binding.edtPhoneNumber.editText?.setText(u.phoneNumber)
        binding.edtHomeAdress.editText?.setText(u.homeAddress)

    }

}