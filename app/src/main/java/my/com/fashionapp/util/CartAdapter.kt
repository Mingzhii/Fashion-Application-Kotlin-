package my.com.fashionapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionapp.R
import my.com.fashionappstaff.data.Cart
import my.com.fashionappstaff.data.Product
import my.com.fashionappstaff.data.totalPrice
import org.w3c.dom.Text

class CartAdapter (val fn: (ViewHolder, Cart) -> Unit = { _, _ -> })
    :  ListAdapter<Cart, CartAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(a: Cart, b: Cart)    = a.cartID == b.cartID
        override fun areContentsTheSame(a: Cart, b: Cart) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgCartProduct)
        val txtName      : TextView = view.findViewById(R.id.txtCartProductName)
        val txtPrice     : TextView = view.findViewById(R.id.txtCartProductPrice)
        val txtQuantity  : TextView = view.findViewById(R.id.txtCartProductQuantity)
        var imgMinus     : ImageButton = view.findViewById(R.id.imgbtnMinus)
        val imgAdd       : ImageButton = view.findViewById(R.id.imgbtnAdd)
        val txtSize      : Button = view.findViewById(R.id.btnCartProductSize)
        val chkBox       : CheckBox = view.findViewById(R.id.chkboxCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = getItem(position)

//        holder.txtId.text   = friend.id

        holder.imgPhoto.setImageBitmap(cart.cartProductPhoto.toBitmap())
        holder.txtName.text = cart.cartProductName
        holder.txtQuantity.text = cart.cartProductQuantity.toString()
        holder.txtSize.text = cart.cartProductSize
        holder.txtPrice.text  = "%.2f".format(cart.cartTotalPrice)
        holder.chkBox.isChecked = cart.cartCheck == "Checked"



        // TODO: Photo (blob to bitmap)a
        fn(holder, cart)
    }
}