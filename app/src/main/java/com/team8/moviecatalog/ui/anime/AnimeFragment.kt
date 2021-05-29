package com.team8.moviecatalog.ui.anime

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team8.moviecatalog.R
import com.team8.moviecatalog.SearchAnimeActivity
import kotlinx.android.synthetic.main.fragment_anime.*

class AnimeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_anime_search.setOnClickListener {
            val intent = Intent(context, SearchAnimeActivity::class.java)
            context?.startActivity(intent)
        }
    }
}