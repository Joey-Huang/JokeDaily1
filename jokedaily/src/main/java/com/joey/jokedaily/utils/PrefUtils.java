package com.joey.jokedaily.utils;

import android.content.Context;

import com.joey.jokedaily.App;

/**
 * SharedPreferences 工具类
 * App  是一个 Application 子类，用于实现全局Context
 */
public class PrefUtils {
    private static String NAME = "config";

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences(NAME, context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static void putInt(String key, int value) {
        putInt(App.getContext(), key, value);
    }

    public static int getInt(Context context, String key, int defValue) {
        return context.getSharedPreferences(NAME, context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getInt(App.getContext(), key, defValue);
    }
    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences(NAME, context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static void putString(String key, String value) {
        putString(App.getContext(), key, value);
    }
    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(NAME, context.MODE_PRIVATE).getString(key, defValue);
    }


    public static String getString(String key, String defValue) {
        return getString(App.getContext(), key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(NAME, context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static void putBoolean(String key, boolean value) {
        putBoolean(App.getContext(), key, value);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(NAME, context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getBoolean(App.getContext(), key, defValue);
    }

}
