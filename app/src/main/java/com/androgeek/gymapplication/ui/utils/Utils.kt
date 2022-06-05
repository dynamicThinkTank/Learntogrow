package com.androgeek.gymapplication.ui.utils

import android.widget.ImageView
import com.androgeek.gymapplication.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

fun ImageView.loadImage(uri : String?) {
    val option = RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
    Glide.with(context).setDefaultRequestOptions(option).load(uri).into(this)
}


