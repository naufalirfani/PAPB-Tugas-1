package com.team8.moviecatalog.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.smarteist.autoimageslider.SliderViewAdapter
import com.team8.moviecatalog.R
import com.team8.moviecatalog.SettingActivity
import com.team8.moviecatalog.models.movie.ResultItem
import com.team8.moviecatalog.utils.loadFromUrl
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*
import kotlin.collections.ArrayList


class SliderAdapter() : SliderViewAdapter<SliderAdapter.Holder>() {

    private var arraySlider = ArrayList<ResultItem?>()

    fun setData(arraySlider: ArrayList<ResultItem?>) {
        this.arraySlider = arraySlider
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return Holder(inflate)
    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {
        val sliderItem = arraySlider[position]


        if(sliderItem?.thumbnail.isNullOrEmpty()){
            Glide.with(viewHolder.itemView).clear(viewHolder.view.iv_auto_image_slider)
            Glide.with(viewHolder.itemView)
                    .load(R.drawable.image_placeholder)
                    .fitCenter()
                    .into(viewHolder.view.iv_auto_image_slider)
        }
        else{
            Glide.with(viewHolder.itemView).clear(viewHolder.view.iv_auto_image_slider)
            Glide.with(viewHolder.itemView)
                    .load(sliderItem?.thumbnail)
                    .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .fitCenter()
                    .into(viewHolder.view.iv_auto_image_slider)
        }

//        viewHolder.itemView.setOnClickListener {
//            val intent = Intent(context, ConcertActivity::class.java)
//            context?.startActivity(intent)
//        }
    }

    override fun getCount(): Int {
        return arraySlider.size
    }

    class Holder(val view: View) : SliderViewAdapter.ViewHolder(view)
}