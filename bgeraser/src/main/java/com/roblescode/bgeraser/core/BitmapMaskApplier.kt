package com.roblescode.bgeraser.core

import android.graphics.*
import androidx.core.graphics.createBitmap

internal object BitmapMaskApplier {

    fun apply(input: Bitmap, mask: Bitmap): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        return createBitmap(mask.width, mask.height).also {
            val canvas = Canvas(it)
            canvas.drawBitmap(input, 0f, 0f, null)
            canvas.drawBitmap(mask, 0f, 0f, paint)
        }
    }
}
