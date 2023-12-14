package com.bangkit.jajanjalan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.jajanjalan.data.remote.response.DataMenuItem
import com.bangkit.jajanjalan.data.remote.response.MenuPenjual
import com.bangkit.jajanjalan.databinding.ItemsProductBinding
import com.bumptech.glide.Glide

class ListMenuPenjualAdapter: RecyclerView.Adapter<ListMenuPenjualAdapter.ListItemMenuViewHolder>() {
    lateinit var onItemClick: ((MenuPenjual) -> Unit)
    inner class ListItemMenuViewHolder(val binding: ItemsProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemMenuViewHolder {
        return ListItemMenuViewHolder(
            ItemsProductBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ListItemMenuViewHolder, position: Int) {
        val listItemMenu = differ.currentList[position]
        if (listItemMenu != null) {
            Glide.with(holder.itemView)
                .load(listItemMenu.image)
                .into(holder.binding.ivMenu)

            holder.binding.apply {
                tvNameProduct.text = listItemMenu.item
                tvPrice.text = "Rp ${listItemMenu.price.toString()} "
            }
            holder.itemView.setOnClickListener {
                onItemClick.invoke(listItemMenu)
            }
        }
        Log.d("Adapter listItemMenu", listItemMenu.toString())
    }

    private val diffUtil: DiffUtil.ItemCallback<MenuPenjual> =
        object : DiffUtil.ItemCallback<MenuPenjual>() {
            override fun areItemsTheSame(oldItem: MenuPenjual, newItem: MenuPenjual): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MenuPenjual, newItem: MenuPenjual): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffUtil)
}