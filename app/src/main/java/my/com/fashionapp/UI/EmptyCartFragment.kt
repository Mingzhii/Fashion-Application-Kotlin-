package my.com.fashionapp.UI

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentEmptyCartBinding


class EmptyCartFragment : DialogFragment() {

    private lateinit var binding : FragmentEmptyCartBinding

    private val nav by lazy{ findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        dialog!!.setCanceledOnTouchOutside(false)

        binding = FragmentEmptyCartBinding.inflate(inflater, container, false)
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.btnEmptyLogin.setOnClickListener { nav.navigate(R.id.action_emptyCartFragment_to_signInFragment) }

        return binding.root
    }

}




