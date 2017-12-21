package com.os.music.player.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.os.music.player.utils.Constants;

/**
 * Created by hue on 24/05/2017.
 */

public class SwitchButtonListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case Constants.Intents.CLOSE_NOTIFICATION:
                Log.d("12345","Hueham");

                break;
            default:
                break;
        }
    }

}
