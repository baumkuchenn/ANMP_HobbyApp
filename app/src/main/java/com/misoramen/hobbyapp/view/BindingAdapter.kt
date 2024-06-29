package com.misoramen.hobbyapp.view

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("android:imageUrl")
fun loadPhotoURL(imageView: ImageView, url: String?) {
    url?.let {
        val picasso = Picasso.Builder(imageView.context)
            .listener { _, _, exception -> exception.printStackTrace() }
            .build()
        picasso.load(it).into(imageView)
        imageView.visibility = View.VISIBLE
    }
}