package com.example.coroutinesapp
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.coroutinesapp.model.IMAGE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {
    //we want to update our imageview so it has to be in the main thread
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)
        //we want to download this image , so we call it on IO the reason for that because its a network comm that has I/O
        coroutineScope.launch {
            val originalDeferred = coroutineScope.async (Dispatchers.IO ) {getOriginalBitMap()}
            //we want to wait for this image to finish downloading so we can use it, once w ehave it we are gonna put it in the imageview
            val originalBitMap = originalDeferred.await()
            loadImage(originalBitMap)
        }
    }

    private fun getOriginalBitMap() =
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun loadImage(bitmap: Bitmap) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bitmap)
        imageView.visibility = View.VISIBLE
        }
}