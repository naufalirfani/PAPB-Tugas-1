package com.team8.moviecatalog.ui.anime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.team8.moviecatalog.MainActivity
import com.team8.moviecatalog.R
import com.team8.moviecatalog.SearchAnimeActivity
import com.team8.moviecatalog.adapter.AnimeAdapter
import com.team8.moviecatalog.adapter.MovieAdapter
import com.team8.moviecatalog.models.anime.AnimeResult
import com.team8.moviecatalog.models.movie.ResultItem
import kotlinx.android.synthetic.main.fragment_anime.*
import kotlinx.android.synthetic.main.fragment_movie_content.*
import java.lang.Exception
import kotlin.random.Random

class AnimeFragment : Fragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private var arrayAnime = ArrayList<AnimeResult?>()
    private lateinit var animeAdapter: AnimeAdapter
    private var backToTOp: ImageButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_anime, container, false)
        animeViewModel = ViewModelProvider(this).get(AnimeViewModel::class.java)
        backToTOp = root.findViewById(R.id.btn_back_to_top)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_anime_bar_title.text = getString(R.string.anime_list)
        btn_anime_search.setOnClickListener {
            val intent = Intent(context, SearchAnimeActivity::class.java)
            context?.startActivity(intent)
        }

        animeViewModel.getAnimeBySearch("", "members", "desc", Random.nextInt(1, 20)).observe({lifecycle}, {
            it?.results?.forEach{anime ->
                arrayAnime.add(anime)
            }
            if(arrayAnime.isNotEmpty()){
                setRecycleview(arrayAnime)
                anime_progress_bar.visibility = View.GONE
            }
        })
    }

    private fun setRecycleview(listAnime: ArrayList<AnimeResult?>){
        val context: Context = requireContext()
        anime_rv.setHasFixedSize(true)
        animeAdapter = AnimeAdapter(context)
        animeAdapter.notifyDataSetChanged()
        animeAdapter.setData(listAnime)
        anime_rv.layoutManager = LinearLayoutManager(context)
        anime_rv.adapter = animeAdapter

        if(backToTOp != null){
            anime_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    //show button when not on top
                    val visibility = if ((anime_rv.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() != 0){
                        View.VISIBLE
                    }
                    else {
                        View.INVISIBLE
                    }
                    backToTOp?.visibility = visibility



                    if(backToTOp?.visibility == View.VISIBLE){
                        backToTOp?.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            backToTOp?.visibility = View.INVISIBLE
                        }, 4000)
                    }

                    //hide layout when scroll down
                    if (dy > 0){
                        backToTOp?.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward_white_24dp, null))
                        //smooth scroll
                        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(activity) {
                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_END
                            }
                        }

                        backToTOp?.setOnClickListener{
                            smoothScroller.targetPosition = listAnime.size
                            (anime_rv.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller)
                            backToTOp!!.visibility = View.INVISIBLE
                        }
                    }
                    else if(dy < 0){
                        backToTOp?.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_white_24dp, null))
                        //smooth scroll
                        val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(activity) {
                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_START
                            }
                        }

                        backToTOp?.setOnClickListener{
                            smoothScroller.targetPosition = 0
                            (anime_rv.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller)
                            backToTOp?.visibility = View.INVISIBLE
                        }
                    }
                }
            })
        }
    }
}