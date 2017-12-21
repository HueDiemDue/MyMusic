package com.os.music.player.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.os.music.player.R;
import com.os.music.player.activity.MainActivity;

import static android.R.attr.id;

/**
 * Created by hue on 24/05/2017.
 */

public class MyNotification {
    private Context context;
//    public MyNotification(Context context){
//        this.context = context;
//    }
    public static void createNotification(Context context, String path, NotificationManager notificationManager){
        //First time
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentText("Hello")
                .setContentTitle(path)
                .setSmallIcon(R.drawable.ic_avt_song)
                .setAutoCancel(false)
//                .setOngoing(running)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(context, 10,
                                new Intent(context, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                0)
                );
//                .addAction(running ? R.drawable.ic_action_pause
//                                : R.drawable.ic_action_play,
//                        running ? context.getString(R.string.pause)
//                                : context.getString(R.string.start),
//                        startPendingIntent)
//                .addAction(R.drawable.ic_action_stop, context.getString(R.string.stop),
//                        stopPendingIntent);

        notificationManager.notify(id, builder.build());

//Second time
        builder.setContentTitle(path);
        notificationManager.notify(id, builder.build());
    }
}
