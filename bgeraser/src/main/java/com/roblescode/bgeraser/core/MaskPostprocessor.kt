package com.roblescode.bgeraser.core

internal object MaskPostprocessor {

    fun flatten(mask: Array<FloatArray>, size: Int): FloatArray {
        val output = FloatArray(size * size)
        var index = 0
        for (y in 0 until size) {
            for (x in 0 until size) {
                output[index++] = mask[y][x]
            }
        }
        return output
    }
}
