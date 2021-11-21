package my.com.fashionapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionapp.R
import my.com.fashionappstaff.data.VoucherClaim

class VoucherClaimAdapter (val fn: (VoucherClaimAdapter.ViewHolder, VoucherClaim) -> Unit = { _, _ -> })
    :  ListAdapter<VoucherClaim, VoucherClaimAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<VoucherClaim>() {
        override fun areItemsTheSame(a: VoucherClaim, b: VoucherClaim)    = a.voucherClaimID == b.voucherClaimID
        override fun areContentsTheSame(a: VoucherClaim, b: VoucherClaim) = a.voucherClaimID == b.voucherClaimID
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val cv           : CardView = view.findViewById(R.id.cv)
        val imgExpiry    : ImageView = view.findViewById(R.id.imgExpiry)
        val imgPhoto     : ImageView = view.findViewById(R.id.imgVoucher)
        val txtName      : TextView = view.findViewById(R.id.txtVoucherName)
        val txtExpiry    : TextView = view.findViewById(R.id.txtExpiryDate)
        val btnClaim     : Button = view.findViewById(R.id.btnClaim)
        val txtTerm      : TextView = view.findViewById(R.id.txtTerm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_voucher, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucherClaim = getItem(position)

        holder.imgPhoto.setImageBitmap(voucherClaim.voucherClaimImg.toBitmap())
        holder.txtName.text = voucherClaim.voucherClaimName
        holder.txtExpiry.text = "Valid " + voucherClaim.voucherClaimExpiryDate

        // TODO: Photo (blob to bitmap)a
        fn(holder, voucherClaim)
    }
}