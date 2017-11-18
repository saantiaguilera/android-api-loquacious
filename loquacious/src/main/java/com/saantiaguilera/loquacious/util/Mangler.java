package com.saantiaguilera.loquacious.util;

/**
 * Created by saguilera on 11/18/17.
 */
public final class Mangler {

    private Mangler() throws IllegalAccessException {
        throw new IllegalAccessException("This class cant be instantiated");
    }

    public static String mangle(String key, int quantity) {
        return Mangler.class.getName() + "_key_" + key + "_times_" + quantity;
    }

    public static String mangle(String key) {
        return Mangler.class.getName() + "_key_" + key;
    }

}
