package com.loantrackingsystem.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.AdhocEmiData
import com.loantrackingsystem.app.databinding.RvAdhocTablevalueBinding
import com.loantrackingsystem.app.databinding.RvEmicalendarBinding
import com.loantrackingsystem.app.databinding.RvPaymentcalendarTablevalueBinding
import com.loantrackingsystem.app.other.Constants
import java.text.SimpleDateFormat


class AdhocTransactionAdapter()  : RecyclerView.Adapter<AdhocTransactionAdapter.LoanHistoryViewHolder>()  {

    class LoanHistoryViewHolder(val binding : RvAdhocTablevalueBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((AdhocEmiData) -> Unit)? = null

    fun setOnItemClickListener(position: (AdhocEmiData) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<AdhocEmiData>() {

        override fun areContentsTheSame(oldItem: AdhocEmiData, newItem: AdhocEmiData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: AdhocEmiData, newItem: AdhocEmiData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var emiCalendarList : List<AdhocEmiData>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LoanHistoryViewHolder {
        val binding = RvAdhocTablevalueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  LoanHistoryViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return emiCalendarList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LoanHistoryViewHolder, position: Int) {
        
        val data = emiCalendarList[position]
        holder.itemView.apply {

            with(holder) {

                binding.tvPaidinterestData.text  = data.paidInterestAmount
                binding.tvPaidprincipleData.text  = "₹" + data.paidPrincipleAmount
                binding.tvEmiDate.text  = getDate(data.date.toLong())

                binding.tvSinoDate.text = getText(position+1)//"${getText(position+1)} Monthly Due Date"


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