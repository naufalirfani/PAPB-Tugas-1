package com.team8.moviecatalog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team8.moviecatalog.adapter.MovieByAdapter
import com.team8.moviecatalog.api.movie.MovieClient
import com.team8.moviecatalog.databinding.ActivitySearchBinding
import com.team8.moviecatalog.models.movie.Movie
import com.team8.moviecatalog.models.movie.ResultItem
import com.team8.moviecatalog.ui.movie.MovieViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

@SuppressLint("ClickableViewAccessibility")
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var movieByAdapter: MovieByAdapter
    private lateinit var movieViewModel: MovieViewModel
    private var arrayMovieBySearch = ArrayList<ResultItem?>()
    private var arrayMovieBySearch2 = ArrayList<ResultItem?>()
    private var currentPage = 1
    private var activity: String? = null
    private var arrayAutoComplete: MutableList<Any?> = mutableListOf()
    private val movieClient: MovieClient = MovieClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        activity = intent.getStringExtra("activity")
        if(activity == "movie"){
            val array: ArrayList<String>? = intent.getStringArrayListExtra("arrayMovieTitle")
            array?.forEach { arrayAutoComplete.add(it) }
            val adapter: ArrayAdapter<*> = ArrayAdapter(this@SearchActivity, android.R.layout.simple_list_item_1, arrayAutoComplete)
            adapter.notifyDataSetChanged()
            binding.etSearch.setAdapter(adapter)
            binding.etSearch.threshold = 1
        }

        searchEvent()
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.isCursorVisible = false
        binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
        binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
        closeKeyBoard()
    }

    private fun searchEvent(){
        initRecycleView()

        binding.etSearch.isCursorVisible = true
        binding.etSearch.requestFocus()
        binding.etSearch.addTextChangedListener {
            binding.etSearch.isCursorVisible = true
            binding.etSearch.requestFocus()
            setActiveBackground()
        }
        binding.etSearch.setOnTouchListener { _, _ ->
            binding.etSearch.isCursorVisible = true
            binding.etSearch.requestFocus()
            setActiveBackground()
            false
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    doSearch(binding.etSearch.text.toString())
                    binding.etSearch.dismissDropDown()
                    binding.etSearch.isCursorVisible = false
                    binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
                    binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
                    closeKeyBoard()
                    true
                }
                else -> false
            }
        }

        binding.btnSearch.setOnClickListener {
            doSearch(binding.etSearch.text.toString())
            binding.etSearch.dismissDropDown()
            binding.etSearch.isCursorVisible = false
            binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
            closeKeyBoard()

        }
    }

    private fun initRecycleView(){
        binding.searchProgressBar.visibility = View.GONE
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.searchRv.layoutManager = gridLayoutManager
        movieByAdapter = MovieByAdapter(this)
        binding.searchRv.adapter = movieByAdapter

        binding.searchRv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadNextPage(binding.etSearch.text.toString())
                    }, 500)
                    binding.searchProgressBar.visibility = View.VISIBLE
                }
                else{
                    binding.searchProgressBar.visibility = View.GONE
                }
            }
        })

        binding.searchRv.setOnTouchListener { _, _ ->
            binding.etSearch.isCursorVisible = false
            binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
            closeKeyBoard()
            false
        }
    }

    private fun doSearch(query: String){
        binding.searchProgressBar.visibility = View.VISIBLE
        movieViewModel.getMovieBySearch(query, currentPage).observe({ lifecycle }, {

            arrayMovieBySearch = it?.result as ArrayList<ResultItem?>

            if(arrayMovieBySearch.isNotEmpty()){
                binding.searchRv.visibility =View.VISIBLE
                binding.searchEmptyState.imgEmptyState.visibility = View.GONE
                binding.searchEmptyState.titleEmptyState.visibility = View.GONE
                binding.searchEmptyState.descEmptyState.visibility = View.GONE
                movieByAdapter.setData(arrayMovieBySearch)
                binding.searchProgressBar.visibility = View.INVISIBLE
            }
            else{
                binding.searchRv.visibility = View.GONE
                enableEmptyState()
                binding.searchProgressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun loadNextPage(query: String){
        currentPage += 1
        arrayMovieBySearch2.clear()
        movieClient.getService().getMovieBySearch(query, currentPage)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.i("Errort", t.message.toString())
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        for(result in response.body()?.result!!){
                            arrayMovieBySearch2.add(result)
                        }
                        movieByAdapter.addData(arrayMovieBySearch2)
                        binding.searchProgressBar.visibility = View.GONE
                    }

                })
    }

    private fun setActiveBackground(){
        val settingActivity = SettingActivity()
        if(settingActivity.getDefaults("isDarkMode", this) == true){
            binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_white, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_white, null))
        }
        else{
            binding.etSearch.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_black, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_black, null))
        }
    }

    private fun enableEmptyState(){
        binding.searchEmptyState.imgEmptyState.visibility = View.VISIBLE
        binding.searchEmptyState.titleEmptyState.visibility = View.VISIBLE
        binding.searchEmptyState.descEmptyState.visibility = View.VISIBLE
        binding.searchEmptyState.imgEmptyState.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_seacrh_404, null))
        binding.searchEmptyState.titleEmptyState.text = getString(R.string.nothing_found)
        binding.searchEmptyState.descEmptyState.text = ""
    }

    private fun closeKeyBoard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }
}