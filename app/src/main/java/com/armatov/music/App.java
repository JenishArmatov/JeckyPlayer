package com.armatov.music;

import android.app.Application;
import android.os.Build;

import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.kabouzeid.appthemehelper.ThemeStore;
import com.armatov.music.appshortcuts.DynamicShortcutManager;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        if (app == null){
            app = this;
        }
        // default theme
        if (!ThemeStore.isConfigured(this, 1)) {
            ThemeStore.editTheme(this)
                    .primaryColorRes(R.color.primary_color)
                    .accentColorRes(R.color.accent_color)
                    .commit();
        }

        // Set up dynamic shortcuts
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            new DynamicShortcutManager(this).initDynamicShortcuts();
        }
    }

    public static App getInstance() {

        return app;
    }
}
