package com.example.meal_prep_app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL

// simple helper for loading meal images from a url
object ImageLoader {

    // downloading the image manually because the coursework avoids image libraries
    fun loadBitmap(urlString: String): Bitmap? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            connection.disconnect()
            bitmap

        // returns null if anything goes wrong while loading
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}