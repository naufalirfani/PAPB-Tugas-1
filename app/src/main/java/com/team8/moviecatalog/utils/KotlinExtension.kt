package com.team8.moviecatalog.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromUrl(path: String) {
    Glide.with(this)
        .load(path)
        .fitCenter()
        .into(this)
}