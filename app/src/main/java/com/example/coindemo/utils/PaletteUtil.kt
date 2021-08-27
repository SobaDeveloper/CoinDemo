package com.example.coindemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest

object PaletteUtil {

    suspend fun getPaletteColor(context: Context, imageUrl: String): Int? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val result2 = loader.execute(request).drawable
        val bitmap = (result2 as BitmapDrawable).bitmap
        return createPaletteSync(bitmap).vibrantSwatch?.rgb
    }

    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()
}