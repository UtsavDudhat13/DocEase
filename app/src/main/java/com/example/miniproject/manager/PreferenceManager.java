package com.example.miniproject.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class PreferenceManager {
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    public static void init() {
        SharedPreferences sharedPreferences;
        PreferenceManager.sharedPreferences = sharedPreferences = AppControl.getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private static SharedPreferences sharedPreferences() {
        return AppControl.getContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
    }
    public static void editor(String str, int i) {
        editor.putInt(str, i);
        editor.apply();
    }

    public static void editor(String str, long l) {
        editor.putLong(str, l);
        editor.apply();
    }

    public static void editor(String str, String string) {
        editor.putString(str, string);
        editor.apply();
    }

    public static void editor(String str, boolean bl) {
        editor.putBoolean(str, bl);
        editor.apply();
    }
    public static void remove(String string2) {
        editor.remove(string2);
        editor.apply();
    }

    public static void clear() {
        editor.clear();
        editor.apply();
    }

    public static Bitmap getUserPic() {
        byte[] arrby = Base64.decode((String) sharedPreferences.getString("UserPic", ""), 11);
        return BitmapFactory.decodeByteArray((byte[]) arrby, (int) 0, (int) arrby.length);
    }

    public static String getEmail(){
        return sharedPreferences.getString("email","");
    }

    public static String getName(){
        return sharedPreferences.getString("name","");
    }

    public static String getMobile(){
        return sharedPreferences.getString("mobile","");
    }
}
