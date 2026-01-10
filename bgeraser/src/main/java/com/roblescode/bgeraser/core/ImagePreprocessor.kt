package com.roblescode.bgeraser.core

import android.graphics.Bitmap
import androidx.core.graphics.scale

internal object ImagePreprocessor {

    private const val SIZE = 320

    private val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    private val std = floatArrayOf(0.229f, 0.224f, 0.225f)

    fun preprocess(bitmap: Bitmap): Pair<FloatArray, Bitmap> {
        val scaled = bitmap.scale(SIZE, SIZE)
        val pixels = IntArray(SIZE * SIZE)
        scaled.getPixels(pixels, 0, SIZE, 0, 0, SIZE, SIZE)

        val input = FloatArray(3 * SIZE * SIZE)

        for (i in pixels.indices) {
            val p = pixels[i]

            val r = ((p shr 16 and 0xFF) / 255f - mean[0]) / std[0]
            val g = ((p shr 8 and 0xFF) / 255f - mean[1]) / std[1]
            val b = ((p and 0xFF) / 255f - mean[2]) / std[2]

            input[i] = r
            input[SIZE * SIZE + i] = g
            input[2 * SIZE * SIZE + i] = b
        }

        return input to scaled
    }
}
