package my.com.fashionapp.UI

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import my.com.fashionapp.databinding.FragmentCartBinding
import my.com.fashionapp.util.CartAdapter
import my.com.fashionapp.util.informationDialog
import my.com.fashionappstaff.data.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val nav by lazy{ findNavController() }
    private val vm: CartViewModel by activityViewModels()
    private val vmU: UserViewModel by activityViewModels()
    private val vmP : ProductViewModel by activityViewModels()

    private lateinit var adapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        vm.getAll()
        vmP.getAll()
        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgCartBack.setOnClickListener { nav.navigate(R.id.action_cartFragment_to_loginProfileFragment) }

        var arrayPress = arrayListOf<OrderList>()

        adapter = CartAdapter() { holder, cart ->

            // Item Add
            holder.imgAdd.setOnClickListener {

                var add = holder.txtQuantity.text.toString().toInt() + 1
                val p = vmP.get(cart.cartProductID)

                if (p?.productQuan!! >= add){
                    val totalPrice = cart.cartProductPrice * add.toString().toDouble()
                    holder.txtQuantity.text = add.toString()

                    val c = Cart (
                        cartID = cart.cartID,
                        cartUsername = cart.cartUsername,
                        cartProductID = cart.cartProductID,
                        cartProductName = cart.cartProductName,
                        cartProductQuantity = add,
                        cartProductPrice = cart.cartProductPrice,
                        cartProductSize = cart.cartProductSize,
                        cartProductPhoto = cart.cartProductPhoto,
                        cartStatus = cart.cartStatus,
                        cartTotalPrice = String.format("%.2f",totalPrice).toDoubleOrNull() ?: 0.0,
                        cartCheck = cart.cartCheck,
                    )
                    vm.set(c)
                } else {
                    val err = "The Product is reached the Maximum Quantity!! \n"
                    informationDialog(err)
                }

            }

            // Item Minus and Validation(need to do)
            holder.imgMinus.setOnClickListener {
                var minus = holder.txtQuantity.text.toString().toInt() - 1
                if (minus != 0){
                    val totalPrice = holder.txtPrice.text.toString().toDouble() - cart.cartProductPrice
                    holder.txtPrice.text = "%.2f".format(totalPrice)
                    holder.txtQuantity.text = minus.toString()
                    val c = Cart (
                        cartID = cart.cartID,
                        cartUsername = cart.cartUsername,
                        cartProductID = cart.cartProductID,
                        cartProductName = cart.cartProductName,
                        cartProductQuantity = minus,
                        cartProductPrice = cart.cartProductPrice,
                        cartProductSize = cart.cartProductSize,
                        cartProductPhoto = cart.cartProductPhoto,
                        cartStatus = cart.cartStatus,
                        cartTotalPrice = String.format("%.2f",totalPrice).toDoubleOrNull() ?: 0.0,
                        cartCheck = cart.cartCheck,
                    )
                    vm.set(c)
                } else {
                    val err = " Do you want to delete the product ? \n"
                    deleteDialog(err, cart.cartID)
                }
            }

            holder.chkBox.setOnClickListener {

                if(holder.chkBox.isChecked){

                    val c = Cart (
                        cartID = cart.cartID,
                        cartUsername = cart.cartUsername,
                        cartProductID = cart.cartProductID,
                        cartProductName = cart.cartProductName,
                        cartProductQuantity = cart.cartProductQuantity,
                        cartProductPrice = cart.cartProductPrice,
                        cartProductSize = cart.cartProductSize,
                        cartProductPhoto = cart.cartProductPhoto,
                        cartStatus = cart.cartStatus,
                        cartTotalPrice = cart.cartTotalPrice,
                        cartCheck = "Checked",
                    )
                    vm.set(c)

                } else {
                    arrayPress.removeIf { c -> c.orderProductID == cart.cartProductID }
                    val c = Cart (
                        cartID = cart.cartID,
                        cartUsername = cart.cartUsername,
                        cartProductID = cart.cartProductID,
                        cartProductName = cart.cartProductName,
                        cartProductQuantity = cart.cartProductQuantity,
                        cartProductPrice = cart.cartProductPrice,
                        cartProductSize = cart.cartProductSize,
                        cartProductPhoto = cart.cartProductPhoto,
                        cartStatus = cart.cartStatus,
                        cartTotalPrice = cart.cartTotalPrice,
                        cartCheck = "Unchecked",
                    )
                    vm.set(c)
                }

            }

        }

        binding.rv.adapter = adapter

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        vm.getAll().observe(viewLifecycleOwner) { list ->

            val cartArray = list.filter { c ->
                c.cartUsername == u?.userName && c.cartStatus != "Done The Payment"
            }

            val caltotal = cartArray.filter { c ->
                c.cartCheck == "Checked"
            }
            if(caltotal.isNotEmpty()){


                var totalPrice = 0.0
                val shipping = 5.0
                var subTotal = 0.0
                arrayPress.clear()

                for(i in 0..caltotal.size - 1){

                    val totalProductPrice = caltotal[i].cartTotalPrice
                    totalPrice += totalProductPrice
                    subTotal = totalPrice + 5.0

                    var ol = OrderList(
                        orderProductID = caltotal[i].cartProductID,
                        orderCartID = caltotal[i].cartID,
                        orderProductQuantity = caltotal[i].cartProductQuantity,
                    )
                    arrayPress.add(ol)
                    arrayPress.size
                }

                binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                binding.txtCartShipping.text = "%.2f".format(shipping)
                binding.txtCartTotalPrice.text = "%.2f".format(subTotal)

            } else {

                val totalPrice = 0.0
                val shipping = 0.0
                val subTotal = 0.0

                binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                binding.txtCartShipping.text = "%.2f".format(shipping)
                binding.txtCartTotalPrice.text = "%.2f".format(subTotal)
            }

            adapter.submitList(cartArray)
        }

        binding.btnCheckOut.setOnClickListener { checkout(arrayPress) }

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)

            if(list.isEmpty()){
                nav.navigate(R.id.emptyCartLogin)
            }
        }

        return binding.root
    }

    private fun checkout(product: ArrayList<OrderList>) {

        checkOutArray = product

        totalPrice = binding.txtCartTotalPrice.text.toString().toDouble()

        nav.navigate(R.id.razorActivity)
    }

    fun Fragment.deleteDialog(text: String, cartID: String) {
        AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage(text)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                vm.delete(cartID)
                dialog.cancel()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            .show()
    }

}