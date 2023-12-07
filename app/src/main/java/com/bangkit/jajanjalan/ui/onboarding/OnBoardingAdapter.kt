package com.bangkit.jajanjalan.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.jajanjalan.databinding.OnboardingItemBinding

class OnBoardingAdapter: RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    private var onBoardingItems: MutableList<OnBoardingItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        with(holder) {
            with(onBoardingItems[position]) {
                binding.textTitle.text = this.title
                binding.imgSlideIcon.setImageResource(this.image)
            }
        }
    }

    fun setNewItem(newDataSet: List<OnBoardingItem>) {
        onBoardingItems.clear()
        onBoardingItems.addAll(newDataSet)
        this.notifyDataSetChanged()
    }

    inner class OnBoardingViewHolder(val binding: OnboardingItemBinding): RecyclerView.ViewHolder(binding.root)

    data class OnBoardingItem(
        val title: String,
        val image: Int
    )
}