package com.roblescode.bgeraser.onnx

import android.content.Context
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import com.roblescode.bgeraser.constants.ModelAssets
import com.roblescode.bgeraser.utils.AssetUtils

internal class OnnxSessionProvider(context: Context) {

    val environment: OrtEnvironment = OrtEnvironment.getEnvironment()

    val session: OrtSession = run {
        val modelFile = AssetUtils.copyAssetToCache(context, ModelAssets.U2NET)
        environment.createSession(modelFile.absolutePath)
    }
}
