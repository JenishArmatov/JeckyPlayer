package com.armatov.music.visualizermusicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class ActivityPerference {

    SharedPreferences prefs;
    public ActivityPerference(Context context){
        prefs = context.getSharedPreferences("com.valdioveliu.valdio.audioplayer.STORAGE", Context.MODE_PRIVATE);
    }


}
