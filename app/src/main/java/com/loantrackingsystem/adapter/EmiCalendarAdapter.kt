package com.loantrackingsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.data.LoanEmiData
import com.loantrackingsystem.app.databinding.RvEmicalendarBinding
import java.text.SimpleDateFormat


class EmiCalendarAdapter()  : RecyclerView.Adapter<EmiCalendarAdapter.LoanHistoryViewHolder>()  {

    class LoanHistoryViewHolder(val binding : RvEmicalendarBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((LoanEmiData) -> Unit)? = null

    fun setOnItemClickListener(position: (LoanEmiData) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<LoanEmiData>() {

        override fun areContentsTheSame(oldItem: LoanEmiData, newItem: LoanEmiData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: LoanEmiData, newItem: LoanEmiData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var emiCalendarList : List<LoanEmiData>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LoanHistoryViewHolder {
        val binding = RvEmicalendarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  LoanHistoryViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return emiCalendarList.size
    }


    override fun onBindViewHolder(holder: LoanHistoryViewHolder, position: Int) {
        
        val data = emiCalendarList[position]
        holder.itemView.apply {

            with(holder) {

                binding.tvStatusData.text  = data.status
                binding.tvAmountData.text  = data.status
                binding.tvEmiDate.text  = data.date

                binding.tvEmiDuecount.text = "${getText(position+1)} Monthly Due Date"

            }

            setOnClickListener {

                onItemClickListener?.let {
                        click ->
                    click(data)
                }
            }
        }
    }

    fun getText(pos : Int) : String{
       return when(pos){
           1 -> "1st"
           2 -> "2nd"
           3 -> "3rd"
           else -> "${pos}th"
        }
    }

    fun getDate(ms : Long) : String{
        val format = SimpleDateFormat("MMM dd, yyyy")
        return format.format(ms)
    }



}