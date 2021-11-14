package my.com.fashionapp.UI

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.DrawableWrapper
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
import my.com.fashionapp.databinding.FragmentProductDetailBinding
import my.com.fashionapp.util.cropToBlob
import my.com.fashionapp.util.errorDialog
import my.com.fashionapp.util.informationDialog
import my.com.fashionapp.util.toBitmap
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.emailAdress
import my.com.fashionappstaff.data.username


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val nav by lazy{ findNavController() }
    private val vm : ProductViewModel by activityViewModels()
    private val vmC: CartViewModel by activityViewModels()
    private val vmU : UserViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id", "N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE


        // TODO
        detail()
        binding.imgProductDetailBack.setOnClickListener { nav.navigateUp() }
        binding.btnAddToCart.setOnClickListener { addToCart() }
        binding.btnSizeXS.setOnClickListener {
            binding.txtSize.text = binding.btnSizeXS.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeS.setOnClickListener {
            binding.txtSize.text = binding.btnSizeS.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeM.setOnClickListener {
            binding.txtSize.text = binding.btnSizeM.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeL.setOnClickListener {
            binding.txtSize.text = binding.btnSizeL.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
        }

        binding.btnSizeXL.setOnClickListener {
            binding.txtSize.text = binding.btnSizeXL.text
            it.setBackgroundColor(Color.rgb(225, 155, 155))
            binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
            binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
        }

        return binding.root
    }

    private fun addToCart() {

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val p = vm.get(id)
        val productPrice = p?.productPrice
        val u = emailLogin?.let { vmU.getEmail(it) }
        val userName = u?.userName

        if (emailLogin == "" && username == ""){
            val err = "You Haven't Sign in An Account. \n"
            errorDialog(err)
        } else {
            if (p?.productQuan != 0){

                if (binding.txtSize.text != "" ){
                    var chkID = vmC.validID()
                    //ATC = Added To Cart
                    //PTP = Process To Payment
                    //DTP = Done The Payment
                    val c = Cart(
                        cartID = chkID,
                        cartUsername = userName.toString(),
                        cartProductID = id,
                        cartProductName = binding.txtProductDetailName.text.toString(),
                        cartProductQuantity = 1,
                        cartProductPrice = productPrice.toString().toDouble(),
                        cartProductSize = binding.txtSize.text.toString(),
                        cartProductPhoto = binding.imgProductDetail.cropToBlob(300,300),
                        cartStatus = "Added To Cart",
                        cartTotalPrice = productPrice.toString().toDouble(),
                        cartCheck = "",
                    )
                    val inform = " Product has added to cart. \n"
                    informationDialog(inform)
                    vmC.set(c)
                    nav.navigate(R.id.action_global_homeFragment)
                }else{
                    val err = " Select the Size of Product. \n"
                    errorDialog(err)
                }
            } else {
                val err = " Out Of Stock\n"
                errorDialog(err)
            }

        }
        binding.btnSizeXS.setBackgroundColor(Color.rgb(250,250,250))
        binding.btnSizeS.setBackgroundColor(Color.rgb(250,250,250))
        binding.btnSizeM.setBackgroundColor(Color.rgb(250,250,250))
        binding.btnSizeL.setBackgroundColor(Color.rgb(250,250,250))
        binding.btnSizeXL.setBackgroundColor(Color.rgb(250,250,250))
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