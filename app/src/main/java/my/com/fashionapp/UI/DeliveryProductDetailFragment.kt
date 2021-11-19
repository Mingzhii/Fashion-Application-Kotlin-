package my.com.fashionapp.UI

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.CartViewModel
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentDeliveryProductDetailBinding
import my.com.fashionapp.databinding.FragmentProductDetailBinding
import my.com.fashionapp.util.cropToBlob
import my.com.fashionapp.util.errorDialog
import my.com.fashionapp.util.informationDialog
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.username


class DeliveryProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryProductDetailBinding
    private val nav by lazy{ findNavController() }
    private val vm : ProductViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmU : UserViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id", "N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentDeliveryProductDetailBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE

        // TODO
        detail()
        binding.imgProductDetailBack.setOnClickListener { nav.navigateUp() }
        return binding.root
    }

    private fun detail() {

        val p = vm.get(id)
        if (p == null){
            nav.navigateUp()
            return
        }

        binding.imgProductDetail.setImageBitmap(p.productPhoto.toBitmap())
        binding.txtProductDetailName.setText(p.productName)
        binding.txtProductDetailPrice.setText("RM %.2f".format(p.productPrice))
        binding.txtProductDetailDescription.setText(p.productDescrip)

    }


}