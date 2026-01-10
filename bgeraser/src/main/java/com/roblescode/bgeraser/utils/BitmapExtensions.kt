package com.roblescode.bgeraser.utils

import android.graphics.Bitmap

fun Bitmap.ensureSoftware(): Bitmap =
    if (config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, false)
    } else {
        this
    }
