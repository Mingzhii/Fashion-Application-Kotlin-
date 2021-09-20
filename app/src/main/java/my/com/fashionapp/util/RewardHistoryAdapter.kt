package my.com.fashionapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.fashionapp.R
import my.com.fashionappstaff.data.ClaimHistory
import my.com.fashionappstaff.data.Reward

class RewardHistoryAdapter (val fn: (ViewHolder, ClaimHistory) -> Unit = { _, _ -> } )
    :  ListAdapter<ClaimHistory, RewardHistoryAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ClaimHistory>() {
        override fun areItemsTheSame(a: ClaimHistory, b: ClaimHistory)    = a.claimHistoryID == b.claimHistoryID
        override fun areContentsTheSame(a: ClaimHistory, b: ClaimHistory) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgReward     : ImageView = view.findViewById(R.id.imgReward)
        val txtRewardName : TextView = view.findViewById(R.id.txtRewardName)
        val txtPoint     : TextView = view.findViewById(R.id.txtRewardPoint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardHistoryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_reward_history, parent, false)
        return RewardHistoryAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardHistoryAdapter.ViewHolder, position: Int) {
        val claimHistory = getItem(position)

        holder.imgReward.setImageBitmap(claimHistory.claimRewardImage.toBitmap())
        holder.txtRewardName.text = claimHistory.claimRewardName
        holder.txtPoint.text = claimHistory.claimRewardPoint.toString()

        fn(holder, claimHistory)
    }


}