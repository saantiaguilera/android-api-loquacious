package com.saantiaguilera.loquacious.resource

import android.util.DisplayMetrics
import android.util.TypedValue
import java.util.*
import java.util.regex.Pattern

object DimensionConverter {

    private val dimensionConstantLookup = initDimensionConstantLookup()
    private val DIMENSION_PATTERN = Pattern.compile("^\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$")

    private fun initDimensionConstantLookup(): Map<String, Int> {
        val metricsMap = HashMap<String, Int>()
        metricsMap.put("px", TypedValue.COMPLEX_UNIT_PX)
        metricsMap.put("dip", TypedValue.COMPLEX_UNIT_DIP)
        metricsMap.put("dp", TypedValue.COMPLEX_UNIT_DIP)
        metricsMap.put("sp", TypedValue.COMPLEX_UNIT_SP)
        metricsMap.put("pt", TypedValue.COMPLEX_UNIT_PT)
        metricsMap.put("in", TypedValue.COMPLEX_UNIT_IN)
        metricsMap.put("mm", TypedValue.COMPLEX_UNIT_MM)
        return Collections.unmodifiableMap(metricsMap)
    }

    fun stringToDimensionPixelSize(dimension: String, metrics: DisplayMetrics): Int {
        // -- Mimics TypedValue.complexToDimensionPixelSize(int data, DisplayMetrics metrics).
        val internalDimension = stringToInternalDimension(dimension)
        val value = internalDimension.value
        val f = TypedValue.applyDimension(internalDimension.unit, value, metrics)
        val res = (f + 0.5f).toInt()
        if (res != 0) return res
        if (value == 0f) return 0
        return if (value > 0) 1 else -1
    }

    fun stringToDimension(dimension: String, metrics: DisplayMetrics): Float {
        // -- Mimics TypedValue.complexToDimension(int data, DisplayMetrics metrics).
        val internalDimension = stringToInternalDimension(dimension)
        return TypedValue.applyDimension(internalDimension.unit, internalDimension.value, metrics)
    }

    private fun stringToInternalDimension(dimension: String): InternalDimension {
        val matcher = DIMENSION_PATTERN.matcher(dimension)
        if (matcher.matches()) {
            val value = java.lang.Float.valueOf(matcher.group(1))!!
            val unit = matcher.group(3).toLowerCase()
            val dimensionUnit = dimensionConstantLookup[unit]
            return if (dimensionUnit == null) {
                throw NumberFormatException()
            } else {
                InternalDimension(value, dimensionUnit)
            }
        } else {
            throw NumberFormatException()
        }
    }

    private class InternalDimension internal constructor(internal var value: Float, internal var unit: Int)
}