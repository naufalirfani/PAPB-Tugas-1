package com.team8.moviecatalog.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.team8.moviecatalog.GenreActivity
import com.team8.moviecatalog.R
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*
import kotlinx.android.synthetic.main.item_movie__data_genre.view.*
import kotlinx.android.synthetic.main.item_movie_genre.view.*
import kotlin.collections.ArrayList


class MovieByGenreAdapter() : RecyclerView.Adapter<MovieByGenreAdapter.Holder>() {

    private var arrayMovieByGenre = ArrayList<ResultItem?>()

    fun setData(array: ArrayList<ResultItem?>) {
        this.arrayMovieByGenre = array
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie__data_genre, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movieByGenre = arrayMovieByGenre[position]

        if(movieByGenre?.thumbnail.isNullOrEmpty()){
            Glide.with(holder.itemView).clear(holder.itemView.movie_genre_image)
            Glide.with(holder.itemView)
                .load(R.drawable.image_placeholder)
                .fitCenter()
                .into(holder.itemView.movie_genre_image)
        }
        else{
            Glide.with(holder.itemView).clear(holder.itemView.movie_genre_image)
            Glide.with(holder.itemView)
                .load(movieByGenre?.thumbnail)
                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                .fitCenter()
                .into(holder.itemView.movie_genre_image)
        }
//        holder.itemView.setOnClickListener {
//            val genreIntent = Intent(holder.itemView.context, GenreActivity::class.java)
//            genreIntent.putExtra("genre", genreText)
//            holder.itemView.context.startActivity(genreIntent)
//        }

    }

    override fun getItemCount(): Int {
        return arrayMovieByGenre.size
    }


    class Holder(view: View) : RecyclerView.ViewHolder(view)
}