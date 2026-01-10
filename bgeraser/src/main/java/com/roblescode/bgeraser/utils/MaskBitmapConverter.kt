package com.roblescode.bgeraser.utils

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

object MaskBitmapConverter {

    fun fromFloatArray(
        mask: FloatArray,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap = createBitmap(width, height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val alpha = (mask[y * width + x] * 255f)
                    .toInt()
                    .coerceIn(0, 255)
                bitmap[x, y] = alpha shl 24
            }
        }
        return bitmap
    }
}