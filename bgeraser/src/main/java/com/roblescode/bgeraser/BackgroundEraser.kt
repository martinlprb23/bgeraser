package com.roblescode.bgeraser

import android.content.Context
import android.graphics.Bitmap
import com.roblescode.bgeraser.onnx.U2NetOnnxRunner
import java.io.Closeable

class BackgroundEraser(context: Context) : Closeable {

    private val runner = U2NetOnnxRunner(context)

    suspend fun clearBackground(image: Bitmap): Result<Bitmap> =
        runCatching {
            runner.process(image)
        }

    override fun close() {
        runner.close()
    }
}
