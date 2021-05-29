package com.team8.moviecatalog.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team8.moviecatalog.GenreActivity
import com.team8.moviecatalog.R


class GenreAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

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
            context.getString(R.string.action),
            context.getString(R.string.adventure),
            context.getString(R.string.animation),
            context.getString(R.string.comedy),
            context.getString(R.string.drama),
            context.getString(R.string.fantasy),
            context.getString(R.string.horror),
            context.getString(R.string.romance),
            context.getString(R.string.scifi),
            context.getString(R.string.thriller),
            context.getString(R.string.western))

    private val arrayGenreTextEng = arrayOf(
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

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View =
            inflater.inflate(R.layout.item_movie_genre, parent, false)
        viewHolder = Holder(viewItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val genreImage = arrayGenreImage[position]
        val genreText = arrayGenreText[position]

        val movieVH = holder as Holder

        movieVH.mGenreTv.text = genreText
        movieVH.mGenreBtn.setImageResource(genreImage)

        movieVH.mGenreBtn.setOnClickListener {
            val genreIntent = Intent(holder.itemView.context, GenreActivity::class.java)
            genreIntent.putExtra("genre", arrayGenreTextEng[position])
            holder.itemView.context.startActivity(genreIntent)
        }

    }

    override fun getItemCount(): Int {
        return arrayGenreText.size
    }


    private inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mGenreTv: TextView = itemView.findViewById<View>(R.id.tv_genre) as TextView
        val mGenreBtn: FloatingActionButton = itemView.findViewById<View>(R.id.btn_genre) as FloatingActionButton

    }
}