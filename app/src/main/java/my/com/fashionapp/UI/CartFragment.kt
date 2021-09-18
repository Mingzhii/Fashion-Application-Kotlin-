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
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentCartBinding
import my.com.fashionapp.util.CartAdapter
import my.com.fashionappstaff.data.OrderList
import my.com.fashionappstaff.data.checkOutArray
import my.com.fashionappstaff.data.totalPrice
import my.com.fashionappstaff.data.username

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val nav by lazy{ findNavController() }
    private val vm: CartViewModel by activityViewModels()
    private val vmU: UserViewModel by activityViewModels()

    private lateinit var adapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // TODO
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        var num = 0
        var product = ""

        binding.imgCartBack.setOnClickListener { nav.navigate(R.id.action_cartFragment_to_loginProfileFragment) }


        var arrayPress = arrayListOf<OrderList>()

        adapter = CartAdapter() { holder, cart ->

            // Item click
//            holder.root.setOnClickListener {
//                nav.navigate(R.id.productDetailFragment, bundleOf("id" to cart.cartID))
//            }
            // Item Add
            holder.imgAdd.setOnClickListener {
                val productPrice = cart.cartProductPrice
                var add = holder.txtQuantity.text.toString().toInt() + 1
                var totalPrice = productPrice * add.toString().toDouble()
                holder.txtPrice.text = "%.2f".format(totalPrice)
                holder.txtQuantity.text = add.toString()
            }

            // Item Minus and Validation(need to do)
            holder.imgMinus.setOnClickListener {

                var minus = holder.txtQuantity.text.toString().toInt() - 1
                if (minus != 0){
                    val productPrice = cart.cartProductPrice
                    var totalPrice = holder.txtPrice.text.toString().toDouble() - cart.cartProductPrice
                    holder.txtPrice.text = "%.2f".format(totalPrice)
                    holder.txtQuantity.text = minus.toString()
                } else {
                    val err = " Do you want to delete the product ? \n"
                    deleteDialog(err, cart.cartID)
                }
            }
            holder.chkBox.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    var ol = OrderList(
                        orderProductID = cart.cartProductID,
                        orderCartID = cart.cartID,
                        orderProductQuantity = holder.txtQuantity.text.toString().toInt(),
                    )
                    arrayPress.add(ol)
                    num += 1
                    var productPrice = holder.txtPrice.text.toString().toDouble()
                    var totalPrice = binding.txtCartSubtotalPrice.text.toString().toDouble()
                    var shipping = 5.0
                    var subtotalprice = 0.0
                    totalPrice += productPrice
                    subtotalprice = totalPrice + shipping
                    binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                    binding.txtCartShipping.text = "%.2f".format(shipping)
                    binding.txtCartTotalPrice.text = "%.2f".format(subtotalprice)

                } else {

                    arrayPress.removeIf { c -> c.orderProductID == cart.cartProductID }
                    num -= 1

                    if (num == 0){

                        var subtotalprice = 0.0
                        var totalPrice = 0.0
                        var shipping = 0.0
                        binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                        binding.txtCartShipping.text = "%.2f".format(shipping)
                        binding.txtCartTotalPrice.text = "%.2f".format(subtotalprice)

                    } else {

                        var productPrice = holder.txtPrice.text.toString().toDouble()
                        var totalPrice = binding.txtCartSubtotalPrice.text.toString().toDouble()
                        totalPrice -= productPrice

                        if (totalPrice == 0.0) {
                            var shipping = 0.0
                            var subtotalprice = binding.txtCartTotalPrice.text.toString().toDouble()
                            subtotalprice -= totalPrice
                            binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                            binding.txtCartShipping.text = "%.2f".format(shipping)
                            binding.txtCartTotalPrice.text = "%.2f".format(subtotalprice)
//                            holder.chkBox.isChecked = false
                        } else {
                            var shipping = 5.0
                            var subtotalprice = binding.txtCartTotalPrice.text.toString().toDouble()
                            subtotalprice = totalPrice + shipping
                            binding.txtCartSubtotalPrice.text = "%.2f".format(totalPrice)
                            binding.txtCartTotalPrice.text = "%.2f".format(subtotalprice)
//                            holder.chkBox.isChecked = false
                        }
                    }

                }

            }
        }

        binding.rv.adapter = adapter
        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        vm.getAll().observe(viewLifecycleOwner) { list ->

            var cartArray = list.filter { c ->
                c.cartUsername == u?.userName && c.cartStatus != "Done The Payment"
            }
            adapter.submitList(cartArray)
        }

        binding.btnCheckOut.setOnClickListener { checkout(arrayPress) }

        return binding.root
    }

    private fun checkout(product: ArrayList<OrderList>) {
        var num = product.size
        num

        checkOutArray = product

        var num2 = checkOutArray.size
        num2

        totalPrice = binding.txtCartTotalPrice.text.toString().toDouble()

        nav.navigate(R.id.paymentMethodFragment)
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