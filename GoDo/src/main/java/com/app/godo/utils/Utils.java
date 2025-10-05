package com.app.godo.utils;

public class Utils {
    public static String extractToken(String header) {
        return header.substring(7);
    }
}
