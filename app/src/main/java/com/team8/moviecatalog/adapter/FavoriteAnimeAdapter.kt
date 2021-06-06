package com.team8.moviecatalog.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.makeramen.roundedimageview.RoundedImageView
import com.team8.moviecatalog.DetailActivity
import com.team8.moviecatalog.R
import com.team8.moviecatalog.database.AppDatabase
import com.team8.moviecatalog.database.anime.AnimeEntity
import com.team8.moviecatalog.database.movie.MovieEntity
import com.team8.moviecatalog.models.anime.AnimeResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavoriteAnimeAdapter(private val context: Context, private val db: AppDatabase) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var mData = ArrayList<AnimeEntity>()
    fun setData(items: ArrayList<AnimeEntity>) {
        mData = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View =
            inflater.inflate(R.layout.item_row_anime_data, parent, false)
        viewHolder = Holder(viewItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val anime = mData[position]

        val animeVH = holder as FavoriteAnimeAdapter.Holder

        Glide.with(context).clear(animeVH.mAnimeImg)
        Glide.with(context)
            .load(anime?.imageUrl)
//                .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888)
//                        .override(Target.SIZE_ORIGINAL))
            .fitCenter()
            .into(animeVH.mAnimeImg)
        animeVH.mAnimeTitleTv.text = anime?.title
        animeVH.mAnimeEpisodeTv.text = "${anime?.type} - ${anime?.episodes} episodes"
        val ratingValue = anime?.score?.toFloat()?.div(2)
        if (ratingValue != null) {
            animeVH.mAnimeRating.rating = ratingValue
        }
        animeVH.mAnimeRatingTv.text = anime?.score.toString()
        animeVH.mAnimeBtn.setOnClickListener {
            val browse = Intent(Intent.ACTION_VIEW, Uri.parse(anime?.url))
            context.startActivity(browse)
        }

        val animeFavorite = AnimeResult(
                anime.endDate.toString(),
                anime.imageUrl.toString(),
                anime.malId,
                anime.synopsis.toString(),
                anime.title.toString(),
                anime.type.toString(),
                anime.url.toString(),
                anime.rated.toString(),
                anime.score,
                anime.members,
                anime.airing,
                anime.episodes,
                anime.startDate.toString()
        )

        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra("anime", animeFavorite)
            detailIntent.putExtra("activity", "anime")
            holder.itemView.context.startActivity(detailIntent)
        }

        holder.itemView.setOnLongClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle(context.getString(R.string.delete_anime))
            builder.setIcon(R.drawable.ic_delete)
            val message = String.format(context.getString(R.string.delete_movie_msg), anime.title)
            builder.setMessage(message)

            builder.setPositiveButton(
                    context.getString(R.string.yes)
            ) { dialog, _ -> // Do nothing but close the dialog
                GlobalScope.launch {
                    db.animeDao().deleteAnime(anime.title.toString())
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


    private inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mAnimeImg: RoundedImageView = itemView.findViewById<View>(R.id.img_anime) as RoundedImageView
        val mAnimeTitleTv: TextView = itemView.findViewById<View>(R.id.tv_anime_title) as TextView
        val mAnimeEpisodeTv: TextView = itemView.findViewById<View>(R.id.tv_anime_episode) as TextView
        val mAnimeRating: SimpleRatingBar = itemView.findViewById<View>(R.id.anime_ratingbar) as SimpleRatingBar
        val mAnimeRatingTv: TextView = itemView.findViewById<View>(R.id.tv_anime_rating) as TextView
        val mAnimeBtn: ImageButton = itemView.findViewById<View>(R.id.btn_anime_page) as ImageButton
    }
}