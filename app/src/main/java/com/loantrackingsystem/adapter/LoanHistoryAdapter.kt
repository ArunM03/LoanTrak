package com.loantrackingsystem.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.databinding.RvLoanhistoryBinding
import com.loantrackingsystem.app.other.Constants
import java.text.SimpleDateFormat

class LoanHistoryAdapter(val isPending : Boolean = false)  : RecyclerView.Adapter<LoanHistoryAdapter.LoanHistoryViewHolder>()  {

    class LoanHistoryViewHolder(val binding : RvLoanhistoryBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((LoanData) -> Unit)? = null

    fun setOnItemClickListener(position: (LoanData) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<LoanData>() {

        override fun areContentsTheSame(oldItem: LoanData, newItem: LoanData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: LoanData, newItem: LoanData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var loanHistoryList : List<LoanData>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  LoanHistoryViewHolder {
        val binding = RvLoanhistoryBinding.inflate(
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

                binding.tvAmount.text = "₹${data.amount}"
                binding.tvName.text = data.name
                binding.tvStatus.text = data.status

                if(isPending){
                    binding.tvStatus.visibility = View.GONE
                }

                if(data.status == Constants.PAID){

                    binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.black))

                }else{

                    binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green))

                }

                binding.tvDate.text = getDate(data.date.toLong())

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