package com.zonek.apps.gfxherror.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */

public class SettingsPreferences {

    static String sound = "soundPref";
    static String music = "musicPref";

    public static void setSound(Context context, Boolean result) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(sound, result);
        prefEditor.apply();
    }

    public static boolean checkSound(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(sound, true);
    }

    public static void setMusic(Context context, Boolean result) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(music, result);
        prefEditor.apply();
    }

    public static boolean checkMusic(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(music, true);
    }







}
