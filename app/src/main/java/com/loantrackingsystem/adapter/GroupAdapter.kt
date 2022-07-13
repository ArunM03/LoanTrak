package com.loantrackingsystem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.LoanPersonData
import com.loantrackingsystem.app.databinding.RvGroupBinding
import com.loantrackingsystem.app.databinding.RvLoanhistoryBinding
import com.loantrackingsystem.app.other.Constants
import java.text.SimpleDateFormat


class GroupAdapter()  : RecyclerView.Adapter<GroupAdapter.LoanHistoryViewHolder>()  {

    class LoanHistoryViewHolder(val binding : RvGroupBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((LoanPersonData) -> Unit)? = null

    fun setOnItemClickListener(position: (LoanPersonData) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<LoanPersonData>() {

        override fun areContentsTheSame(oldItem: LoanPersonData, newItem: LoanPersonData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: LoanPersonData, newItem: LoanPersonData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var loanHistoryList : List<LoanPersonData>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LoanHistoryViewHolder {
        val binding = RvGroupBinding.inflate(
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

                binding.tvName.text = data.name
                binding.tvNumber.text = data.phone

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
        val format = SimpleDateFormat("MMM dd, yyyy")
        return format.format(ms)
    }



}