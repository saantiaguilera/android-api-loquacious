package com.saantiaguilera.loquacious.resource;

import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DimensionConverter {

    private static final Map<String, Integer> dimensionConstantLookup = initDimensionConstantLookup();
    private static final Pattern DIMENSION_PATTERN = Pattern.compile("^\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$");

    @NonNull
    private static Map<String, Integer> initDimensionConstantLookup() {
        Map<String, Integer> metricsMap = new HashMap<String, Integer>();
        metricsMap.put("px", TypedValue.COMPLEX_UNIT_PX);
        metricsMap.put("dip", TypedValue.COMPLEX_UNIT_DIP);
        metricsMap.put("dp", TypedValue.COMPLEX_UNIT_DIP);
        metricsMap.put("sp", TypedValue.COMPLEX_UNIT_SP);
        metricsMap.put("pt", TypedValue.COMPLEX_UNIT_PT);
        metricsMap.put("in", TypedValue.COMPLEX_UNIT_IN);
        metricsMap.put("mm", TypedValue.COMPLEX_UNIT_MM);
        return Collections.unmodifiableMap(metricsMap);
    }

    public static int stringToDimensionPixelSize(@NonNull String dimension, @NonNull DisplayMetrics metrics) {
        // -- Mimics TypedValue.complexToDimensionPixelSize(int data, DisplayMetrics metrics).
        InternalDimension internalDimension = stringToInternalDimension(dimension);
        final float value = internalDimension.value;
        final float f = TypedValue.applyDimension(internalDimension.unit, value, metrics);
        final int res = (int)(f+0.5f);
        if (res != 0) return res;
        if (value == 0) return 0;
        if (value > 0) return 1;
        return -1;
    }

    public static float stringToDimension(@NonNull String dimension, @NonNull DisplayMetrics metrics) {
        // -- Mimics TypedValue.complexToDimension(int data, DisplayMetrics metrics).
        InternalDimension internalDimension = stringToInternalDimension(dimension);
        return TypedValue.applyDimension(internalDimension.unit, internalDimension.value, metrics);
    }

    @NonNull
    private static InternalDimension stringToInternalDimension(@NonNull String dimension) {
        Matcher matcher = DIMENSION_PATTERN.matcher(dimension);
        if (matcher.matches()) {
            float value = Float.valueOf(matcher.group(1));
            String unit = matcher.group(3).toLowerCase();
            Integer dimensionUnit = dimensionConstantLookup.get(unit);
            if (dimensionUnit == null) {
                throw new NumberFormatException();
            } else {
                return new InternalDimension(value, dimensionUnit);
            }
        } else {
            throw new NumberFormatException();
        }        
    }

    private static class InternalDimension {
        float value;
        int unit;

        InternalDimension(float value, int unit) {
            this.value = value;
            this.unit = unit;
        }
    }
}