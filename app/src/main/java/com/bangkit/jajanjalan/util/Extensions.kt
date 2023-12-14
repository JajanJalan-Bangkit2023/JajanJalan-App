package com.bangkit.jajanjalan.util

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.ui.MainActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun ImageView.glide(url: String) {
    Glide.with(this).load(url).into(this)
}

fun Fragment.hideBottomNavView() {
    val appBar: BottomAppBar = (activity as MainActivity).findViewById(R.id.menuBottom)
    val frameLayout: FrameLayout = (activity as MainActivity).findViewById(R.id.frameLayout)
    val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.btnLiveLocation)

    appBar.visibility = View.GONE
    frameLayout.visibility = View.GONE
    fab.visibility = View.GONE
}

fun Fragment.showBottomNavView() {
    val appBar: BottomAppBar = (activity as MainActivity).findViewById(R.id.menuBottom)
    val frameLayout: FrameLayout = (activity as MainActivity).findViewById(R.id.frameLayout)
    val fab: FloatingActionButton = (activity as MainActivity).findViewById(R.id.btnLiveLocation)

    appBar.visibility = View.VISIBLE
    frameLayout.visibility = View.VISIBLE
    fab.visibility = View.VISIBLE
}