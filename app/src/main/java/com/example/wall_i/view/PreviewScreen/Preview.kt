

package com.example.wall_i.view.PreviewScreen

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.wall_i.R
import kotlinx.android.synthetic.main.activity_preview.*


class Preview : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1000
   // var url:Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        download.setOnClickListener {
            checkPermission()
        }
        share.setOnClickListener {
            onShareButtonClicked(intent.getStringExtra("image"))
        }


        var requestoptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
        Glide.with(this)
            .applyDefaultRequestOptions(requestoptions)
            .load(intent.getStringExtra("image"))
            .into(preview)

    }

    private fun startDownloading() {

        val request = DownloadManager.Request(Uri.parse(intent.getStringExtra("image")))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Downloading")
        request.setDescription("Image is being downloaded")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,"${System.currentTimeMillis()}".plus(".jpg"))
        Log.d("Myapp","${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission is granted , start downloading
                    startDownloading()
                } else {
                    //permission denied, show error
                    Toast.makeText(this, "Storage Permission is Required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    fun favbuttonClicked(view: View) {

        favorite.setImageResource(R.drawable.ic_favorite_black_24dp)
        //add the image into database
    }
    fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                //permission is denied , Request it back
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)

            } else {
                //permission already granted start downloading
                startDownloading()
            }

        } else {
            //system os is less than marshmallo, runtime permission is not required , just start downloading
            startDownloading()
        }

    }

    fun onShareButtonClicked(link:String){
        var shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey Check this awsome image\n".plus(link))
            //this.`package`="com.whatsapp"
            type = "image/plain"
            flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags=Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        }
        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
    }


}
