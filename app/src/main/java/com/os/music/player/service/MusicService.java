package com.os.music.player.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.os.music.player.R;
import com.os.music.player.activity.PlayMusicActivity;
import com.os.music.player.database.DBManager;
import com.os.music.player.shareprefrences.PreferenceUtils;
import com.os.music.player.utils.Constants;

import static com.os.music.player.database.DBManager.INSTANCE;

/**
 * Created by binhnk on 5/26/2017.
 */

public class MusicService extends Service {
    private Context mContext;
    public NotificationManager mNotificationManager;
    public NotificationCompat.Builder builder = null;
    public Notification notif = null;
    private RemoteViews remoteViews = null;
    private BroadcastReceiver mReceiveNoti;
    private String mSongId = "";

    private MediaPlayer player;
    private String SONG_PATH = "";
    private String SONG_ID = "";
    private String SONG_NAME_ALBUMS = "";
    private String SONG_NAME_ARTIST = "";
    private int CURRENT_POSITION = 0;
    private int FRAGMENT_TYPE = 0;

    private IntentFilter filter;
    private Bitmap bitmap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        mContext = getApplicationContext();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        filter = new IntentFilter(Constants.Intents.PLAY_SONG_CLICKED);
        filter.addAction(Constants.Intents.PAUSE_CURRENT_SONG);
        filter.addAction(Constants.Intents.RESUME_CURRENT_SONG);
        filter.addAction(Constants.Intents.PLAY_NEXT_SONG);
        filter.addAction(Constants.Intents.PLAY_PREV_SONG);
        filter.addAction(Constants.Intents.SEEK_POSITION_SONG);
        filter.addAction(Constants.Intents.REQUEST_PROGRESS);
        filter.addAction(Constants.Intents.CLOSE_NOTIFICATION);
        filter.addAction(Constants.Intents.PAUSE);
        mContext.registerReceiver(mReceiver, filter);
        Log.d(this.getClass().getSimpleName(), " onCretae ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            // receive from adapter
            FRAGMENT_TYPE = intent.getIntExtra(Constants.Intents.FRAGMENT_TYPE, 0);
            PreferenceUtils.save(Constants.Intents.FRAGMENT_TYPE, FRAGMENT_TYPE, mContext);
            SONG_ID = intent.getStringExtra(Constants.Intents.ID);
            mSongId = SONG_ID;
            createNotification();
            startForeground(1, notif);
            refreshDataTv(remoteViews, DBManager.INSTANCE.getSong(SONG_ID).getNameSong(), DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
            SONG_PATH = INSTANCE.getSong(SONG_ID).getData();
            CURRENT_POSITION = intent.getIntExtra(Constants.Intents.CURRENT_POSITION, 0);
            SONG_NAME_ALBUMS = INSTANCE.getSong(SONG_ID).getNameAlbum();
            SONG_NAME_ARTIST = INSTANCE.getSong(SONG_ID).getNameArist();
            Log.d(this.getClass().getSimpleName(), "tét ");
            if (CURRENT_POSITION == 0) {
                playSong(SONG_PATH);
            } else {

                if (player.isPlaying()) {
                    player.seekTo(CURRENT_POSITION);
                    player.start();
                    Log.d(this.getClass().getSimpleName(), " is playing");
                } else {
                    Log.d(this.getClass().getSimpleName(), " play again");
                    playSong(SONG_PATH);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CURRENT_POSITION = PreferenceUtils.getIntFromPreference(Constants.Intents.CURRENT_POSITION, mContext);
            switch (intent.getAction()) {
                case Constants.Intents.PLAY_SONG_CLICKED:
//                    remoteViews.setImageViewResource(R.id.tvPauseNoti,R.drawable.ic_pause);
//                    if (!player.isPlaying()) {
//                        SONG_ID = intent.getStringExtra(Constants.Intents.ID);
//                        Log.d("MusicService", CURRENT_POSITION + ",id: " + SONG_ID + " , check_play ");
//                        mSongId = SONG_ID;
//                        createNotification();
//                        refreshDataTv(remoteViews, DBManager.INSTANCE.getSong(SONG_ID).getNameSong(), DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
//                        player.seekTo(CURRENT_POSITION);
//                        player.start();
//                    }
                    break;

                case Constants.Intents.PAUSE_CURRENT_SONG:
                    pauseCurrentSong();
                    Log.d("MusicService", "pause " + CURRENT_POSITION + ",id: " + SONG_ID + " , check_play ");
                    remoteViews.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_play);
                    mNotificationManager.notify(1, notif);
//                    mNotificationManager.cancel(1);

                    break;

                case Constants.Intents.PAUSE:
                    if (player.isPlaying()) {
                        pauseCurrentSong();
                        remoteViews.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_play);
                        mNotificationManager.notify(1, notif);
                        sendBroadcast(new Intent(Constants.Intents.SET_TOGGLE_OFF));
                    } else {
                        player.seekTo(CURRENT_POSITION);
                        player.start();
                        remoteViews.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_pause);
                        mNotificationManager.notify(1, notif);
                        sendBroadcast(new Intent(Constants.Intents.SET_TOGGLE_ON));
                    }
                    break;

                case Constants.Intents.PLAY_NEXT_SONG:
                    actionNext(intent);
                    remoteViews.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_pause);
                    mNotificationManager.notify(1, notif);
                    Log.d("MusicService", "play_next_song_done");
                    sendBroadcast(new Intent(Constants.Intents.SET_TOGGLE_ON));
                    break;

                case Constants.Intents.PLAY_PREV_SONG:
                    actionPrev(intent);
                    remoteViews.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_pause);
                    mNotificationManager.notify(1, notif);
                    sendBroadcast(new Intent(Constants.Intents.SET_TOGGLE_ON));
                    break;

                case Constants.Intents.SEEK_POSITION_SONG:
                    actionSeekPosition(intent);
                    break;

                case Constants.Intents.REQUEST_PROGRESS:
                    actionRequestProgress();
                    break;

                case Constants.Intents.CLOSE_NOTIFICATION:
                    mNotificationManager.cancel(1);
                    stopSelf();
                    Intent sendIntent = new Intent(Constants.Intents.SET_TOGGLE_OFF);
                    sendBroadcast(sendIntent);
                    break;

                default:
                    break;
            }
        }
    };


    private void playSong(final String path) {
        try {
            if (player.isPlaying()) {
                player.stop();

            }
            player.reset();
            player.setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                String nextPath = getNextPath(path);
                playSong(nextPath);
                SONG_ID = INSTANCE.getIdSong(nextPath);
                refreshDataTv(remoteViews, DBManager.INSTANCE.getSong(SONG_ID).getNameSong(), DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
            }
        });

        player.start();
    }

    private void pauseCurrentSong() {
        if (player.isPlaying()) {
            player.pause();
            CURRENT_POSITION = player.getCurrentPosition();
        }
    }


    private void actionSeekPosition(Intent intent) {
        int pos = intent.getIntExtra(Constants.Intents.SEEK_POSITION_POS, 0);
        if (player.isPlaying()) {
            player.pause();
            player.seekTo(pos);
            player.start();
        }
    }

    private void actionNext(Intent intent) {
//        SONG_ID = intent.getStringExtra(Constants.Intents.ID);
//        if (SONG_ID_Intent.equals("")) {
//            SONG_ID = PreferenceUtils.getStringFromPreference(Constants.Intents.ID, mContext);
//        } else {
//            SONG_ID = SONG_ID_Intent;
//        }
        SONG_PATH = getNextPath(INSTANCE.getSong(SONG_ID).getData());
        playSong(SONG_PATH);
        Log.d("Song_path", SONG_PATH);
        SONG_ID = INSTANCE.getIdSong(SONG_PATH);
        Log.d("SONG_PATH", SONG_PATH + " SONG_ID " + SONG_ID);
        refreshDataTv(remoteViews, DBManager.INSTANCE.getSong(SONG_ID).getNameSong(), DBManager.INSTANCE.getSong(SONG_ID).getNameArist());

    }

    private void actionPrev(Intent intent) {
        String SONG_ID_Intent = intent.getStringExtra(Constants.Intents.ID);
        if (SONG_ID_Intent.equals("")) {
            SONG_ID = PreferenceUtils.getStringFromPreference(Constants.Intents.ID, mContext);
        } else {
            SONG_ID = SONG_ID_Intent;
        }
        SONG_PATH = getPrevPath(DBManager.INSTANCE.getSong(SONG_ID).getData());
        playSong(SONG_PATH);
        SONG_ID = DBManager.INSTANCE.getIdSong(SONG_PATH);
        refreshDataTv(remoteViews, DBManager.INSTANCE.getSong(SONG_ID).getNameSong(), DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
    }

    private void actionRequestProgress() {
//        if (player.isPlaying()) {
        Intent i = new Intent(Constants.Intents.RECEIVE_PROGRESS);
        i.putExtra(Constants.Intents.CURRENT_POSITION, player.getCurrentPosition());
        i.putExtra(Constants.Intents.DURATION, player.getDuration());
        i.putExtra(Constants.Intents.ID, SONG_ID);
        i.putExtra(Constants.Intents.FRAGMENT_TYPE,FRAGMENT_TYPE);
//            PreferenceUtils.save(Constants.Intents.CURRENT_POSITION, player.getCurrentPosition(), mContext);
//            PreferenceUtils.save(Constants.Intents.ID,SONG_ID,mContext);
        sendBroadcast(i);
//        }
    }

    private String getNextPath(String oldPath) {
        String new_path = "";
        Boolean check_shuffer = PreferenceUtils.getBooleanFromPreference(Constants.Intents.SHUFFER, getBaseContext());
        Boolean check_repeat = PreferenceUtils.getBooleanFromPreference(Constants.Intents.REPEAT, getBaseContext());
        if (check_shuffer && check_repeat || !check_repeat && check_shuffer) {
            switch (FRAGMENT_TYPE) {
                case 1:
                    new_path = INSTANCE.getRandomPathAlbums(SONG_NAME_ALBUMS);
                    break;
                case 2:
                    new_path = INSTANCE.getRandomPathArtist(SONG_NAME_ARTIST);
                    break;
                case 4:
                    new_path = INSTANCE.getRandomPathLike();
                    break;
                default:
                    new_path = INSTANCE.getRandomPath();
                    break;
            }
        } else if (check_repeat) {
            new_path = oldPath;
        } else if (!check_repeat && !check_shuffer) {
            switch (FRAGMENT_TYPE) {
                case 1:
                    new_path = INSTANCE.getNextPathAlbums(oldPath, SONG_NAME_ALBUMS);
                    break;
                case 3:
                    new_path = INSTANCE.getNextPathArtist(oldPath, SONG_NAME_ARTIST);
                    break;
                case 4:
                    new_path = INSTANCE.getNextPathLike(SONG_PATH);
                    break;
                default:
                    SONG_ID = INSTANCE.getIdSong(oldPath);
                    new_path = INSTANCE.getNextPath(SONG_ID);
                    break;
            }
        }
        return new_path;
    }

    private String getPrevPath(String oldPath) {
        String new_path = "";
        Boolean check_shuffer = PreferenceUtils.getBooleanFromPreference(Constants.Intents.SHUFFER, getBaseContext());
        Boolean check_repeat = PreferenceUtils.getBooleanFromPreference(Constants.Intents.REPEAT, getBaseContext());
        if (check_shuffer && check_repeat || !check_repeat && check_shuffer) {
            switch (FRAGMENT_TYPE) {
                case 1:
                    new_path = INSTANCE.getRandomPathAlbums(SONG_NAME_ALBUMS);
                    break;
                case 2:
                    new_path = INSTANCE.getRandomPathArtist(SONG_NAME_ARTIST);
                    break;
                case 4:
                    new_path = INSTANCE.getRandomPathLike();
                    break;
                default:
                    new_path = INSTANCE.getRandomPath();
                    break;
            }
        } else if (check_repeat) {
            new_path = oldPath;
        } else if (!check_repeat && !check_shuffer) {
            switch (FRAGMENT_TYPE) {
                case 1:
                    new_path = INSTANCE.getPrevPathAlbums(oldPath, SONG_NAME_ALBUMS);
                    break;
                case 3:
                    new_path = INSTANCE.getPrevPathArtist(oldPath, SONG_NAME_ARTIST);
                    break;
                case 4:
                    new_path = INSTANCE.getPrevPathLike(SONG_PATH);
                    break;
                default:
                    SONG_ID = INSTANCE.getIdSong(oldPath);
                    new_path = INSTANCE.getPrevPath(SONG_ID);
                    break;
            }
        }
        return new_path;
    }

    public void createNotification() {
        Intent notifyIntent =
                new Intent(this, PlayMusicActivity.class);
        int current_position = PreferenceUtils.getIntFromPreference(Constants.Intents.CURRENT_POSITION, mContext);
        notifyIntent.putExtra(Constants.Intents.REQUEST_START_SERVICE, true);
        notifyIntent.putExtra(Constants.Intents.ID, mSongId);
        Log.d(this.getClass().getSimpleName(), " song_id " + mSongId);
        notifyIntent.putExtra(Constants.Intents.CURRENT_POSITION, current_position);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        remoteViews = new RemoteViews(getBaseContext().getPackageName(),
                R.layout.notification_button);
        builder = new NotificationCompat.Builder(this)
                .setContentIntent(notifyPendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setContent(remoteViews)
                .setCustomBigContentView(remoteViews);
        setUpRemoteview(remoteViews, SONG_ID);
        notif = builder.setContentTitle("MyMusic")
                .setContentText("Listen music!!!").setAutoCancel(false).build();
        notif.flags |= Notification.FLAG_NO_CLEAR;
        actionRemoteView(remoteViews);
//        receiveNotification();
    }

    // setup ban đầu
    private void setUpRemoteview(RemoteViews remoteview, String id) {
        remoteview.setTextViewText(R.id.tvNameNoti, DBManager.INSTANCE.getSong(id).getNameSong());
        remoteview.setTextViewText(R.id.tvNameSingerNoti, DBManager.INSTANCE.getSong(id).getNameArist());
        remoteview.setImageViewResource(R.id.tvPauseNoti, R.drawable.ic_pause);
        bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.ic_avt_song);
        builder.setLargeIcon(bitmap);
    }

    // sự kiện khi click vào các button trên notification
    public void actionRemoteView(RemoteViews remoteViews) {
        //close
        Intent exitReceive = new Intent(Constants.Intents.CLOSE_NOTIFICATION);
        PendingIntent pendingIntentClose = PendingIntent.getBroadcast(this, 0, exitReceive, 0);
        builder.addAction(R.id.tvCloseNoti, Constants.Intents.CLOSE_NOTIFICATION, pendingIntentClose);
        builder.setContentIntent(pendingIntentClose);
        remoteViews.setOnClickPendingIntent(R.id.tvCloseNoti, pendingIntentClose);

        // pause/resume
        Intent pauseIntent = new Intent(Constants.Intents.PAUSE);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(this, 1, pauseIntent, 0);
        builder.addAction(R.id.tvPauseNoti, Constants.Intents.PAUSE, pendingIntentPause);
        builder.setContentIntent(pendingIntentPause);
        remoteViews.setOnClickPendingIntent(R.id.tvPauseNoti, pendingIntentPause);

        // next
        Intent nextIntent = new Intent(Constants.Intents.PLAY_NEXT_SONG);
        nextIntent.putExtra(Constants.Intents.ID, SONG_ID);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 2, nextIntent, 0);
        builder.addAction(R.id.tvNextSongNoti, Constants.Intents.PLAY_NEXT_SONG, pendingIntentNext);
        builder.setContentIntent(pendingIntentNext);
        remoteViews.setOnClickPendingIntent(R.id.tvNextSongNoti, pendingIntentNext);
        // prev
        Intent prevIntent = new Intent(Constants.Intents.PLAY_PREV_SONG);
        prevIntent.putExtra(Constants.Intents.ID, SONG_ID);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(this, 3, prevIntent, 0);
        builder.addAction(R.id.tvPrevNoti, Constants.Intents.PLAY_NEXT_SONG, pendingIntentPrev);
        builder.setContentIntent(pendingIntentPrev);
        remoteViews.setOnClickPendingIntent(R.id.tvPrevNoti, pendingIntentPrev);

    }

    public void refreshDataTv(RemoteViews remoteViews, String name_song, String name_singer) {
        remoteViews.setTextViewText(R.id.tvNameNoti, name_song);
        remoteViews.setTextViewText(R.id.tvNameSingerNoti, name_singer);
        mNotificationManager.notify(1, notif);
    }


    public void cancelNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy ");
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        try {
//            player.stop();
            player.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cancelNotification();
        super.onDestroy();
    }

}
