package com.team8.moviecatalog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Base64
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team8.moviecatalog.adapter.AnimeAdapter
import com.team8.moviecatalog.api.anime.AnimeClient
import com.team8.moviecatalog.api.anime.SearchAnimeClient
import com.team8.moviecatalog.databinding.ActivitySearchAnimeBinding
import com.team8.moviecatalog.models.ImageBody
import com.team8.moviecatalog.models.anime.*
import com.team8.moviecatalog.models.movie.ResultItem
import com.team8.moviecatalog.ui.anime.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_anime.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class SearchAnimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAnimeBinding
    private val ALLOW_KEY = "ALLOWED"
    private val STORAGE_PREF = "storage_pref"
    private var filePath: Uri? = null
    private var currentPhotoPath: String = "";
    private val CAMERA_REQUEST = 100
    private val GALLERY_REQUEST = 101
    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private lateinit var animeViewModel: AnimeViewModel
    private var arrayDocs = ArrayList<DocsItem?>()
    private lateinit var animeAdapter: AnimeAdapter
    private val animeClient: AnimeClient = AnimeClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySearchAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animeViewModel = ViewModelProvider(this).get(AnimeViewModel::class.java)

        supportActionBar?.hide()

        permission()

        searchEvent()

        enableEmptyState()

        animeAdapter = AnimeAdapter(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_ALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@SearchAnimeActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(this@SearchAnimeActivity, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED)) {
                        if (!getFromPref(this, ALLOW_KEY)!!) {
                            saveToPreferences(this, ALLOW_KEY, true)
                        }
                        binding.btnAnimeCamera.setOnClickListener {
                            dispatchTakePictureIntent()
                        }
                    }
                    else{
                        saveToPreferences(this, ALLOW_KEY, false)
                        binding.btnAnimeCamera.setOnClickListener {
                            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    saveToPreferences(this, ALLOW_KEY, false)
                    binding.btnAnimeCamera.setOnClickListener {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
                return
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                val imgFile = File(currentPhotoPath)
                filePath = Uri.fromFile(imgFile)
                val source = filePath?.let { ImageDecoder.createSource(this.contentResolver, it) }
                val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                getSearchResult(bitmap?.let { encodeTobase64(it).toString() }.toString())
            }
            GALLERY_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                filePath = data!!.data
                val source = filePath?.let { ImageDecoder.createSource(this.contentResolver, it) }
                val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                getSearchResult(bitmap?.let { encodeTobase64(it).toString() }.toString())
            }
        }
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    class Item(val text: String, val icon: Int) {
        override fun toString(): String {
            return text
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        val options =
            arrayOf<CharSequence>("Camera", "Choose from Gallery")
        val items = arrayOf(
            Item("Camera", R.drawable.photo),
            Item("Choose from gallery", R.drawable.gallery))
        val adapter: ListAdapter = object : ArrayAdapter<Item?>(
            this,
            android.R.layout.select_dialog_item,
            android.R.id.text1,
            items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                //Use super class to create the View
                val v: View = super.getView(position, convertView, parent)
                val tv = v.findViewById(android.R.id.text1) as TextView
                tv.setTextSize(16F)

                //Put the image on the TextView
                val image: Drawable
                val res: Resources = resources
                image = res.getDrawable(items.get(position).icon)
                image.setBounds(0, 0, 100, 100)
                tv.setCompoundDrawables(image, null, null, null)

                //Add margin between image and text (support various screen densities)
                val dp5 = (15 * resources.displayMetrics.density + 0.5f).toInt()
                tv.compoundDrawablePadding = dp5

                return v
            }
        }
        val cw = ContextThemeWrapper(this, R.style.AlertDialogTheme)
        AlertDialog.Builder(cw)
            .setAdapter(adapter) { dialog, item ->
                when {
                    options[item] == "Camera" -> {
                        openCamera()
                        dialog.dismiss()
                    }
                    options[item] == "Choose from Gallery" -> {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST)
                        dialog.dismiss()
                    }
                }
            }.show()
    }

    private fun openCamera() {
//        val cameraIntent =
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, CAMERA_REQUEST)

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.team8.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun permission(){
        if (!hasPermissions(this, *PERMISSIONS)) {
            if (getFromPref(this, ALLOW_KEY)!!) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }
            else if (!hasPermissions(this, *PERMISSIONS)) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
                } else {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
                }
            }
        }
        else{
            if (getFromPref(this, ALLOW_KEY)!!) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }
        }
    }

    fun saveToPreferences(
        context: Context,
        key: String?,
        allowed: Boolean?
    ) {
        val myPrefs: SharedPreferences = context.getSharedPreferences(
            STORAGE_PREF,
            Context.MODE_PRIVATE
        )
        val prefsEditor: SharedPreferences.Editor = myPrefs.edit()
        if (allowed != null) {
            prefsEditor.putBoolean(key, allowed)
        }
        prefsEditor.apply()
    }

    fun getFromPref(context: Context, key: String?): Boolean? {
        val myPrefs = context.getSharedPreferences(
            STORAGE_PREF,
            Context.MODE_PRIVATE
        )
        return myPrefs.getBoolean(key, false)
    }

    private fun getSearchResult(base64: String){
        disableEmptyState()
        binding.searchAnimeProgressBar.visibility = View.VISIBLE

        animeViewModel.searchAnime(base64).observe({lifecycle}, {
            arrayDocs = it?.docs as ArrayList<DocsItem?>

            if(arrayDocs.isNotEmpty()){
                val arrayMalId = ArrayList<Int?>()
                arrayDocs.forEach{arrayMalId.add(it?.malId)}
                val listMalId =  arrayMalId.distinct()
                val listAnimeDetail = ArrayList<AnimeResult?>()
                var i = 0
                for(malId in listMalId){
                    animeViewModel.getByid("/v3/anime/$malId").observe({lifecycle}, {
                        val animeDetail = it

                        if(animeDetail != null){
                            i += 1
                            if(i % listMalId.size == 0){
                                val animeResult = AnimeResult(
                                    null,
                                    animeDetail.imageUrl,
                                    animeDetail.malId,
                                    animeDetail.synopsis,
                                    animeDetail.title,
                                    animeDetail.type,
                                    animeDetail.url,
                                    animeDetail.rating,
                                    animeDetail.score,
                                    animeDetail.members,
                                    animeDetail.airing,
                                    animeDetail.episodes,
                                    null

                                )
                                listAnimeDetail.add(animeResult)
                            }
                            if(i == listMalId.size * listMalId.size){
                                setRecycleView(listAnimeDetail)
                                binding.searchRvAnime.visibility = View.VISIBLE
                                binding.searchAnimeProgressBar.visibility = View.INVISIBLE
                            }
                        }
                        else{
                            i += 1
                            binding.searchRvAnime.visibility = View.GONE
                            enableEmptyState()
                            binding.searchAnimeProgressBar.visibility = View.INVISIBLE
                        }
                    })
                }
            }
            else{
                binding.searchRvAnime.visibility = View.GONE
                enableEmptyState()
                binding.searchAnimeProgressBar.visibility = View.INVISIBLE
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun searchEvent(){

        binding.etSearchAnime.isCursorVisible = true
        binding.etSearchAnime.requestFocus()
        binding.etSearchAnime.addTextChangedListener {
            binding.etSearchAnime.isCursorVisible = true
            binding.etSearchAnime.requestFocus()
            setActiveBackground()
        }
        binding.etSearchAnime.setOnTouchListener { _, _ ->
            binding.etSearchAnime.isCursorVisible = true
            binding.etSearchAnime.requestFocus()
            setActiveBackground()
            false
        }

        binding.etSearchAnime.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    doSearch(binding.etSearchAnime.text.toString())
                    binding.etSearchAnime.dismissDropDown()
                    binding.etSearchAnime.isCursorVisible = false
                    binding.etSearchAnime.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
                    binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
                    closeKeyBoard()
                    true
                }
                else -> false
            }
        }

        binding.btnSearch.setOnClickListener {
            doSearch(binding.etSearchAnime.text.toString())
            binding.etSearchAnime.dismissDropDown()
            binding.etSearchAnime.isCursorVisible = false
            binding.etSearchAnime.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_grey, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_grey, null))
            closeKeyBoard()

        }
    }

    private fun doSearch(query: String){
        disableEmptyState()
        binding.searchAnimeProgressBar.visibility = View.VISIBLE
        val arrayAnime = ArrayList<AnimeResult?>()
        animeClient.getService().getAnimeBySearch(query, "members", "desc", 1)
            .enqueue(object : Callback<Anime> {
                override fun onFailure(call: Call<Anime>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(call: Call<Anime>, response: Response<Anime>) {
                    if(response.code() == 200){
                        disableEmptyState()
                        response.body()?.results?.forEach{anime ->
                            arrayAnime.add(anime)
                        }
                        setRecycleView(arrayAnime)
                        binding.searchRvAnime.visibility = View.VISIBLE
                        binding.searchAnimeProgressBar.visibility = View.INVISIBLE
                    }
                    else{
                        println("kosong")
                        setRecycleView(arrayAnime)
                        binding.searchRvAnime.visibility = View.INVISIBLE
                        enableEmptyState()
                        binding.searchAnimeProgressBar.visibility = View.INVISIBLE
                    }

                }

            })
    }

    private fun setRecycleView(listAnime: ArrayList<AnimeResult?>){
        binding.searchRvAnime.setHasFixedSize(true)
        animeAdapter.setData(listAnime)
        animeAdapter.notifyDataSetChanged()
        binding.searchRvAnime.layoutManager = LinearLayoutManager(this)
        binding.searchRvAnime.adapter = animeAdapter
    }

    private fun setActiveBackground(){
        val settingActivity = SettingActivity()
        if(settingActivity.getDefaults("isDarkMode", this) == true){
            binding.etSearchAnime.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_white, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_white, null))
        }
        else{
            binding.etSearchAnime.background = ResourcesCompat.getDrawable(resources, R.drawable.edttext_style_black, null)
            binding.btnSearch.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_black, null))
        }
    }

    private fun closeKeyBoard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

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

    private fun disableEmptyState(){
        binding.searchEmptyState.imgEmptyState.visibility = View.GONE
        binding.searchEmptyState.titleEmptyState.visibility = View.GONE
        binding.searchEmptyState.descEmptyState.visibility = View.GONE
        binding.searchEmptyState.imgEmptyState.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_seacrh_404, null))
        binding.searchEmptyState.titleEmptyState.text = getString(R.string.nothing_found)
        binding.searchEmptyState.descEmptyState.text = ""
    }
}