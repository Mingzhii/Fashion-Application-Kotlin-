package my.com.fashionapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionapp.R
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionappstaff.data.Order

class OrderAdapter(val fn: (OrderAdapter.ViewHolder, Order) -> Unit = { _, _ -> })
    :  ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(a: Order, b: Order)    = a.orderId == b.orderId
        override fun areContentsTheSame(a: Order, b: Order) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto     : ImageView = view.findViewById(R.id.imgOrderProduct)
        val txtName      : TextView = view.findViewById(R.id.txtOrderProductName)
        val txtPrice     : TextView = view.findViewById(R.id.txtOrderProductPrice)
        val txtQuantity  : TextView = view.findViewById(R.id.txtOrderProductQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
//
//        val orderproduct = order.orderProduct
//        val divide = ","
//        val list = orderproduct.split(divide)


//        holder.txtId.text   = friend.id





        // TODO: Photo (blob to bitmap)a
        fn(holder, order)
    }
}