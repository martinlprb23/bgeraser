package com.roblescode.bgeraser.utils

import android.content.Context
import java.io.File

internal object AssetUtils {

    fun copyAssetToCache(context: Context, fileName: String): File {
        val cacheFile = File(context.cacheDir, fileName)

        if (!cacheFile.exists()) {
            context.assets.open(fileName).use { input ->
                cacheFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return cacheFile
    }
}
