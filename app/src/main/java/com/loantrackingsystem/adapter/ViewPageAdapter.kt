package com.loantrackingsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.loantrackingsystem.app.databinding.RvLoanhistoryBinding
import com.loantrackingsystem.app.databinding.VpImageBinding


class ViewPageAdapter() : RecyclerView.Adapter<ViewPageAdapter.PlaylistViewHolder>() {
    
    inner class PlaylistViewHolder(val binding : VpImageBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Int>(){

        override fun areItemsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Int,
            newItem: Int
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var playlist: List<Int>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemClickListener2: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemClickListener2(listener: (Int) -> Unit) {
        onItemClickListener2 = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPageAdapter.PlaylistViewHolder {
        val binding = VpImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val data = playlist[position]
        holder.itemView.apply {

                with(holder) {

                    binding.ivImage.setImageResource(data)

                }

        }
    }

}