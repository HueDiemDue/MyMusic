package com.os.music.player.application;

import android.app.Application;
import android.content.Context;

import com.mz.ZAndroidSystemDK;
import com.os.music.player.database.DBManager;

/**
 * Created by hue on 18/05/2017.
 */

public class MyMusicApplication extends Application {
    public Context getAppContext(){
        return (Context) getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.INSTANCE.init(getApplicationContext());

        ZAndroidSystemDK.initApplication(this,getPackageName());
    }
}
