package com.nearme.onlinesdk.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class PrivacyUtil {
    private static final String KEY_PRIVACY_STATE = "KEY_PRIVACY_STATE";
    private final static String SP_NAME = "oppo_demo";

    public static boolean isPrivacyAgreed(Context context) {
        if(context ==null) return false;
        SharedPreferences oppoDemo = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return oppoDemo.getBoolean(KEY_PRIVACY_STATE,false);
    }

    public static void savePrivacyAgreed(Context context){
        if(context ==null) return ;
        SharedPreferences oppoDemo = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        oppoDemo.edit().putBoolean(KEY_PRIVACY_STATE,true).apply();
    }
}
