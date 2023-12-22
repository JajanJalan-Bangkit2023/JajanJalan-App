package com.bangkit.jajanjalan.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.jajanjalan.data.remote.response.DataMenuItem
import com.bangkit.jajanjalan.databinding.ListItemProductBinding
import com.bumptech.glide.Glide

class ListItemMenuAdapter: RecyclerView.Adapter<ListItemMenuAdapter.ListItemMenuViewHolder>() {
    lateinit var onItemClick: ((DataMenuItem) -> Unit)
    inner class ListItemMenuViewHolder(val binding: ListItemProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemMenuViewHolder {
        return ListItemMenuViewHolder(
            ListItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListItemMenuViewHolder, position: Int) {
        val listItemMenu = differ.currentList[position]
        if (listItemMenu.menu != null) {
            val rating = String.format("%.1f", listItemMenu.menu.rating)

            Glide.with(holder.itemView)
                .load(listItemMenu.menu.image)
                .into(holder.binding.ivProduct)

            holder.binding.apply {
                menuName.text = listItemMenu.menu.item
                menuPrice.text = "Rp${listItemMenu.menu.price.toString()}"
                sellerName.text = listItemMenu.penjual?.name
                tvRating.text = rating
            }
            holder.itemView.setOnClickListener {
                onItemClick.invoke(listItemMenu)
            }
        }
        Log.d("Adapter listItemMenu", listItemMenu.toString())
    }

    private val diffUtil: DiffUtil.ItemCallback<DataMenuItem> =
        object : DiffUtil.ItemCallback<DataMenuItem>() {
            override fun areItemsTheSame(oldItem: DataMenuItem, newItem: DataMenuItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataMenuItem, newItem: DataMenuItem): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffUtil)
}