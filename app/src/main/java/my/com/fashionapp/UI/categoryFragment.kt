package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentCartBinding
import my.com.fashionapp.databinding.FragmentCategoryBinding


class categoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val nav by lazy{ findNavController() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.VISIBLE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.relativeMen.setOnClickListener { nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Men")) }

        binding.relativeWomen.setOnClickListener { nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Women")) }



        return binding.root
    }

}