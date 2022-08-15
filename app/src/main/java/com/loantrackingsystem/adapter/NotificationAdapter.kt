package com.loantrackingsystem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.NotificationDataForUser
import com.loantrackingsystem.app.databinding.RvGroupBinding
import com.loantrackingsystem.app.databinding.RvLoanhistoryBinding
import com.loantrackingsystem.app.databinding.RvNotificationBinding
import com.loantrackingsystem.app.other.Constants
import java.text.SimpleDateFormat


class NotificationAdapter()  : RecyclerView.Adapter<NotificationAdapter.LoanHistoryViewHolder>()  {

    class LoanHistoryViewHolder(val binding : RvNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((NotificationDataForUser) -> Unit)? = null

    fun setOnItemClickListener(position: (NotificationDataForUser) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<NotificationDataForUser>() {

        override fun areContentsTheSame(oldItem: NotificationDataForUser, newItem: NotificationDataForUser): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: NotificationDataForUser, newItem: NotificationDataForUser): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var loanHistoryList : List<NotificationDataForUser>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LoanHistoryViewHolder {
        val binding = RvNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  LoanHistoryViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return loanHistoryList.size
    }


    override fun onBindViewHolder(holder: LoanHistoryViewHolder, position: Int) {
        
        val data = loanHistoryList[position]
        holder.itemView.apply {

            with(holder) {
                
                binding.tvMessage.text = data.message
                binding.tvTime.text = getDate(data.time.toLong())

            }

            setOnClickListener {

                onItemClickListener?.let {
                        click ->
                    click(data)
                }
            }
        }
    }

    fun getDate(ms : Long) : String{
        val format = SimpleDateFormat("MMM dd")
        return format.format(ms)
    }



}