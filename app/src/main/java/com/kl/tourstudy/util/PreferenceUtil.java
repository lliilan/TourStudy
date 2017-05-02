package com.kl.tourstudy.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 记录类，储存公共数据
 * Created by KL on 2016/11/30 0030.
 */

public class PreferenceUtil {

    public static final String IP = "http://192.168.1.133:8080/";
    public static final String PROJECT = "study_tour/";
    public static final String ERROR = "error";

    /**
     * 是否显示第一次引导界面，true为显示，false，默认不显示
     */
    public static final String SHOW_FIRST_SLIDE = "show";

    public static void setBoolean(Context context, String key, boolean value){
        //参数第一个为文件名，第二个为类型
        SharedPreferences preferences = context.getSharedPreferences("preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        // 返回key值，key值默认值是false
        return preferences.getBoolean(key, false);
    }
}

