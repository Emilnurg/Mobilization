package ru.s4nchez.translater;

import android.content.Context;
import android.content.SharedPreferences;

public class SP {

    private static final String FILE_SAVE = "data";


    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_SAVE, context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }
}