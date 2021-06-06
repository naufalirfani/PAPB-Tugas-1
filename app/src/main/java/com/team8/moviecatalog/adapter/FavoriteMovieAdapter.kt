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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.team8.moviecatalog.DetailActivity
import com.team8.moviecatalog.R
import com.team8.moviecatalog.TrailerActivity
import com.team8.moviecatalog.database.AppDatabase
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.item_row_favorite_data.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class FavoriteMovieAdapter(private val context: Context, private val db: AppDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var mData = ArrayList<MovieEntity>()
    fun setData(items: ArrayList<MovieEntity>) {
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

        val videoId = movieFav.trailer?.replace("https://www.youtube.com/watch?v=", "")
        holder.itemView.btn_favorite_trailer.setOnClickListener {
            val intent = Intent(context, TrailerActivity::class.java)
            intent.putExtra("videoId", videoId)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra("movie", movieFavorite)
            detailIntent.putExtra("activity", "favorite")
            holder.itemView.context.startActivity(detailIntent)
        }

        holder.itemView.setOnLongClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle(context.getString(R.string.delete_movie))
            builder.setIcon(R.drawable.ic_delete)
            val message = String.format(context.getString(R.string.delete_movie_msg), movieFav.title)
            builder.setMessage(message)

            builder.setPositiveButton(
                context.getString(R.string.yes)
            ) { dialog, _ -> // Do nothing but close the dialog
                GlobalScope.launch {
                    db.movieDao().deleteMovie(movieFav.title.toString())
                }
                if(mData.size == 1){
                    mData.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, mData.size)
                }
                Toast.makeText(context, context.getString(R.string.delete_from_favorite), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            builder.setNegativeButton(
                context.getString(R.string.no)
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