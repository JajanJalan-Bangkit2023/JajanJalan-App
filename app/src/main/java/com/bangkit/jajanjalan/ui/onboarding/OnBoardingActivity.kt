package com.bangkit.jajanjalan.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.databinding.ActivityOnboardingBinding
import com.bangkit.jajanjalan.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var onBoardingViewPager: ViewPager2
    private lateinit var layoutOnBoardingIndicators: LinearLayout
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBoardingViewPager = binding.onBoardingViewPager
        layoutOnBoardingIndicators = binding.dots
        btnNext = binding.btnNext
        supportActionBar?.hide()

        setUpOnBoardingItems()
        onBoardingViewPager.adapter = onBoardingAdapter

        setUpOnBoardingItems()
        setUpOnBoardingIndicators()
        setCurrentOnBoardingIndicators(0)

        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnBoardingIndicators(position)
            }
        })

        btnNext.setOnClickListener {
            if (onBoardingViewPager.currentItem + 1 < onBoardingAdapter.itemCount) {
                onBoardingViewPager.currentItem = onBoardingViewPager.currentItem + 1
            } else {
                val intent = Intent(this@OnBoardingActivity, AuthActivity::class.java)
                startActivity(intent)
                finishAffinity()
                onBoardingFinished()
                this.finish()
            }
        }
    }

    private fun setUpOnBoardingItems() {
        val onBoardingItems: MutableList<OnBoardingAdapter.OnBoardingItem> = ArrayList()

        val onBoarding1 = OnBoardingAdapter.OnBoardingItem(
            resources.getString(R.string.first_slide_title),
            R.drawable.ob_1
        )
        val onBoarding2 = OnBoardingAdapter.OnBoardingItem(
            resources.getString(R.string.second_slide_title),
            R.drawable.ob_2
        )
        val onBoarding3 = OnBoardingAdapter.OnBoardingItem(
            resources.getString(R.string.third_slide_title),
            R.drawable.ob_3
        )

        onBoardingItems.add(onBoarding1)
        onBoardingItems.add(onBoarding2)
        onBoardingItems.add(onBoarding3)

        onBoardingAdapter = OnBoardingAdapter()
        onBoardingAdapter.setNewItem(onBoardingItems)
    }

    private fun setUpOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            layoutOnBoardingIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentOnBoardingIndicators(index: Int) {
        val childCount = layoutOnBoardingIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = layoutOnBoardingIndicators[i] as ImageView

            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
        if (index == onBoardingAdapter.itemCount - 1) {
            btnNext.text = resources.getString(R.string.get_started)
        } else {
            btnNext.text = resources.getString(R.string.next)
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}