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

class PaymentProductAdapter (val fn: (ViewHolder, Cart) -> Unit = { _, _ -> })
    :  ListAdapter<Cart, PaymentProductAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(a: Cart, b: Cart)    = a.cartID == b.cartID
        override fun areContentsTheSame(a: Cart, b: Cart) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto        : ImageView = view.findViewById(R.id.imgProduct)
        val txtProductName  : TextView = view.findViewById(R.id.txtProductName)
        val txtSize         : TextView = view.findViewById(R.id.txtSize)
        val txtProductPrice : TextView = view.findViewById(R.id.txtProductPrice)
        var txtQuantity     : TextView = view.findViewById(R.id.txtQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_payproduct, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payment = getItem(position)

        holder.imgPhoto.setImageBitmap(payment.cartProductPhoto.toBitmap())
        holder.txtProductName.text = payment.cartProductName
        holder.txtQuantity.text = "x" + payment.cartProductQuantity.toString()
        holder.txtSize.text = payment.cartProductSize
        holder.txtProductPrice.text  = "RM "+"%.2f".format(payment.cartTotalPrice)

        fn(holder, payment)
    }
}