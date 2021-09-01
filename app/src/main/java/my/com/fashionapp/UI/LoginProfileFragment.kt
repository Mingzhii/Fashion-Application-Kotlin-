package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentLoginProfileBinding

class LoginProfileFragment : Fragment() {

    private lateinit var binding: FragmentLoginProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLoginProfileBinding.inflate(inflater, container, false)

        // TODO


        return binding.root
    }

}