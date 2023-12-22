package com.bangkit.jajanjalan.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity
import com.bangkit.jajanjalan.data.remote.response.DataMenuItem
import com.bangkit.jajanjalan.databinding.ListItemProductBinding
import com.bangkit.jajanjalan.databinding.ListItemSellerBinding
import com.bumptech.glide.Glide

class FavoriteSellerAdapter: RecyclerView.Adapter<FavoriteSellerAdapter.ViewHolder>() {
    lateinit var onItemClick: ((FavoriteEntity) -> Unit)
    inner class ViewHolder(val binding: ListItemSellerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSellerBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seller = differ.currentList[position]
        if (seller != null) {
            Glide.with(holder.itemView)
                .load(seller.image)
                .into(holder.binding.ivSeller)

            holder.binding.apply {
                sellerName.text = seller.name
                sellerAddress.text = seller.address
            }
            holder.itemView.setOnClickListener {
                onItemClick.invoke(seller)
            }
        }
        Log.d("Adapter List Seller", seller.toString())
    }

    private val diffUtil: DiffUtil.ItemCallback<FavoriteEntity> =
        object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, diffUtil)
}