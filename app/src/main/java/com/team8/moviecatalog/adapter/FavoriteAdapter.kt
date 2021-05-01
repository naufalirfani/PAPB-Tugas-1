package com.team8.moviecatalog.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.team8.moviecatalog.DetailActivity
import com.team8.moviecatalog.R
import com.team8.moviecatalog.database.movie.AppMovieDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class FavoriteAdapter(private val context: Context, private val db: AppMovieDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var mData: List<MovieEntity> = mutableListOf()
    fun setData(items: List<MovieEntity>) {
        mData = listOf()
        mData = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View =
                inflater.inflate(R.layout.item_row_favorite_data, parent, false)
        viewHolder = MovieVH(viewItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieFav = mData[position] // Movie
        val movieVH = holder as MovieVH

        movieVH.mMovieTv.text = movieFav.title
        movieVH.mMovieDescTv.text = movieFav.duration
        Glide.with(context).clear(movieVH.mPosterImg)
        Glide.with(context)
                .load(movieFav.thumbnail)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
                .fitCenter()
                .into(movieVH.mPosterImg)

        var genre: List<String> = mutableListOf()
        val movieFavorite = ResultItem(
            movieFav.duration,
            movieFav.trailer,
            movieFav.thumbnail.toString(),
            movieFav.watch.toString(),
            genre,
            movieFav.rating.toString(),
            movieFav.title.toString(),
            movieFav.quality.toString()
        )

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra("movie", movieFavorite)
            detailIntent.putExtra("activity", "favorite")
            holder.itemView.context.startActivity(detailIntent)
        }

        holder.itemView.setOnLongClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("Delete Movie")
            builder.setIcon(R.drawable.ic_delete)
            val message = HtmlCompat.fromHtml("Apakah Anda ingin menghapus " + "<b>" + movieFav.title + "</b>" + " dari favorite?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            builder.setMessage(message)

            builder.setPositiveButton(
                "Ya"
            ) { dialog, _ -> // Do nothing but close the dialog
                GlobalScope.launch {
                    db.movieDao().deleteMovie(movieFav.title.toString())
                }
                Toast.makeText(context, "Dihapus dari favorite", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            builder.setNegativeButton(
                "Tidak"
            ) { dialog, _ -> // Do nothing
                dialog.dismiss()
            }

            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.colorSecondary, typedValue, true)
            val color = ContextCompat.getColor(context, typedValue.resourceId)

            val alert: AlertDialog = builder.create()
            alert.setOnShowListener {
                alert.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(color)
            }
            alert.show()

            return@setOnLongClickListener true
        }
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    private inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mPosterImg: RoundedImageView = itemView.findViewById<View>(R.id.img_favorite_data) as RoundedImageView
        val mMovieTv: TextView = itemView.findViewById<View>(R.id.tv_favorite_data_title) as TextView
        val mMovieDescTv: TextView = itemView.findViewById<View>(R.id.tv_favorite_data_desc) as TextView

    }
}