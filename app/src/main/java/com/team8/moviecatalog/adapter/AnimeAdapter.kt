package com.team8.moviecatalog.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.makeramen.roundedimageview.RoundedImageView
import com.team8.moviecatalog.R
import com.team8.moviecatalog.models.anime.AnimeResult


class AnimeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var animeResults = ArrayList<AnimeResult?>()

    fun setData(animeResults: ArrayList<AnimeResult?>) {
        this.animeResults = animeResults
        notifyDataSetChanged()
        println(this.animeResults[5]?.title)
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
        val anime = animeResults[position]

        val animeVH = holder as AnimeAdapter.Holder

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
    }

    override fun getItemCount(): Int {
        return animeResults.size
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