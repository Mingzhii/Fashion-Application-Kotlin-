package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentPaymentMethodBinding

class PaymentMethodFragment : Fragment() {

    private lateinit var binding: FragmentPaymentMethodBinding
    private val nav by lazy{ findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)

        binding.btnPayByCard.setOnClickListener { nav.navigate(R.id.payByCardFragment) }

        binding.btnTNG.setOnClickListener {  }

        return binding.root
    }

}