package com.team8.moviecatalog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.team8.moviecatalog.databinding.ActivitySearchAnimeBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class SearchAnimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAnimeBinding
    private val ALLOW_KEY = "ALLOWED"
    private val STORAGE_PREF = "storage_pref"
    private lateinit var firebaseDatabase: FirebaseDatabase
    private var storageReference: StorageReference? = null
    private var filePath: Uri? = null
    private var photo: Bitmap? = null
    private var currentPhotoPath: String = "";
    private val CAMERA_REQUEST = 100
    private val GALLERY_REQUEST = 101
    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySearchAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        permission()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                val imgFile = File(currentPhotoPath)
                filePath = Uri.fromFile(imgFile)
                uploadImage()
            }
            GALLERY_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                filePath = data!!.data
                uploadImage()
            }
        }
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

    private fun uploadImage(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialoguploadphoto)
        progressDialog.setCancelable(true)
        progressDialog.show()
        val progressbarUpload: ProgressBar = progressDialog.findViewById(R.id.setting_progress_bar)

        if(filePath != null){
            val fileNameList = filePath.toString().split("/")
            val fileName = fileNameList[fileNameList.size - 1]
            val ref = storageReference?.child("imageAnime/${fileName}")
            ref?.putFile(filePath!!)?.addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener {
                    val url = it.toString()
                    Toast.makeText(this, url, Toast.LENGTH_LONG).show()
                }.addOnFailureListener {}
                progressDialog.dismiss()
                Toast.makeText(this, resources.getString(R.string.processing_images_succes), Toast.LENGTH_LONG).show()
            }?.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, resources.getString(R.string.processing_images_failed) + e.message, Toast.LENGTH_LONG).show()
            }?.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressbarUpload.progress = progress.toInt()
            }
        }else{
            Toast.makeText(this, resources.getString(R.string.select_image), Toast.LENGTH_LONG).show()
        }
    }
}