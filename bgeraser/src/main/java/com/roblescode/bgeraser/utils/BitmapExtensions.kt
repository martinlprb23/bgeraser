package com.roblescode.bgeraser.utils

import android.graphics.Bitmap
import android.os.Build

fun Bitmap.ensureSoftware(): Bitmap =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, false)
    } else {
        this
    }
