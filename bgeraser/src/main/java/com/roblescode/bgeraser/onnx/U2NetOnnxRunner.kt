package com.roblescode.bgeraser.onnx

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import com.roblescode.bgeraser.core.BitmapMaskApplier
import com.roblescode.bgeraser.core.ImagePreprocessor
import com.roblescode.bgeraser.core.MaskPostprocessor
import com.roblescode.bgeraser.utils.MaskBitmapConverter
import com.roblescode.bgeraser.utils.ensureSoftware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer

internal class U2NetOnnxRunner(context: Context) {

    private val session = OnnxSessionProvider(context).session
    private val size = 320
    private val environment = OrtEnvironment.getEnvironment()

    internal suspend fun process(image: Bitmap): Bitmap =
        withContext(Dispatchers.IO) {
            processInternal(image)
        }

    private fun processInternal(image: Bitmap): Bitmap {
        val safeImage = image.ensureSoftware()
        val (inputArray, _) = ImagePreprocessor.preprocess(safeImage)

        val tensor = OnnxTensor.createTensor(
            environment,
            FloatBuffer.wrap(inputArray),
            longArrayOf(1, 3, size.toLong(), size.toLong())
        )

        return try {
            val result = session.run(mapOf(session.inputNames.first() to tensor))
            val output = result[0].value as Array<*>

            @Suppress("UNCHECKED_CAST")
            val mask2D = ((output[0] as Array<*>)[0] as Array<FloatArray>)

            val flatMask = MaskPostprocessor.flatten(mask2D, size)

            val maskBitmap = MaskBitmapConverter
                .fromFloatArray(flatMask, size, size)
                .scale(image.width, image.height)

            result.close()

            BitmapMaskApplier.apply(image, maskBitmap)
        } finally {
            tensor.close()
        }
    }

    fun close() {
        session.close()
    }
}
