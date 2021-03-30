package com.team8.moviecatalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team8.moviecatalog.R
import com.team8.moviecatalog.models.movie.ResultItem


open class MovieByGenreAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var movieResults = ArrayList<ResultItem?>()
    private var isLoadingAdded = false


    fun setData(moveResults: ArrayList<ResultItem?>) {
        this.movieResults = moveResults
        notifyDataSetChanged()
    }

    fun addData(movieResults: ArrayList<ResultItem?>) {
        this.movieResults.addAll(movieResults)
        notifyItemInserted(this.movieResults.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View =
                inflater.inflate(R.layout.item_movie__data_genre, parent, false)
        viewHolder = MovieVH(viewItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieByGenre = movieResults[position] // Movie
        val movieVH = holder as MovieVH
        Glide.with(context).clear(movieVH.mPosterImg)
        Glide.with(context)
                .load(movieByGenre?.thumbnail)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
                .fitCenter()
                .into(movieVH.mPosterImg)
    }

    override fun getItemCount(): Int {
        return movieResults.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    private inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mPosterImg: ImageView = itemView.findViewById<View>(R.id.movie_genre_image) as ImageView

    }

    protected inner class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }
}