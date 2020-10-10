package com.example.intimesimple.utils

import kotlin.math.abs
import kotlin.math.max

fun calculateRadiusOffset(
        strokeSize: Float,
        dotStrokeSize: Float,
        markerStrokeSize: Float
): Float {
    return max(strokeSize, max(dotStrokeSize, markerStrokeSize))
}
