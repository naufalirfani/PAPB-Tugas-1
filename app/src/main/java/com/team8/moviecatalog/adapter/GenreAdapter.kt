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
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*
import kotlinx.android.synthetic.main.item_movie_genre.view.*
import kotlin.collections.ArrayList


class GenreAdapter() : RecyclerView.Adapter<GenreAdapter.Holder>() {

    private val arrayGenreImage = arrayOf(
            R.drawable.genre_action,
            R.drawable.genre_adventure,
            R.drawable.genre_animation,
            R.drawable.genre_comedy,
            R.drawable.genre_drama,
            R.drawable.genre_fantasy,
            R.drawable.genre_horror,
            R.drawable.genre_romance,
            R.drawable.genre_scifi,
            R.drawable.genre_thriller,
            R.drawable.genre_western)

    private val arrayGenreText = arrayOf(
            "Action",
            "Adventure",
            "Animation",
            "Comedy",
            "Drama",
            "Fantasy",
            "Horror",
            "Romance",
            "Sci-Fi",
            "Thriller",
            "Western")

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie_genre, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val genreImage = arrayGenreImage[position]
        val genreText = arrayGenreText[position]

        holder.itemView.tv_genre.text = genreText
        holder.itemView.btn_genre.setImageResource(genreImage)

        holder.itemView.btn_genre.setOnClickListener {
            val genreIntent = Intent(holder.itemView.context, GenreActivity::class.java)
            genreIntent.putExtra("genre", genreText)
            holder.itemView.context.startActivity(genreIntent)
        }

    }

    override fun getItemCount(): Int {
        return arrayGenreText.size
    }


    class Holder(view: View) : RecyclerView.ViewHolder(view)
}