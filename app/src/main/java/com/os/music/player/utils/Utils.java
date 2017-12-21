package com.os.music.player.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

/**
 * Created by binhnk on 5/22/2017.
 */

public class Utils {

    public static int checkPermission(String[] permissions, Context mContext) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(mContext, permission);
        }
        return permissionCheck;
    }

    public static String getCoverArtPath(long albumId, Context context) {
        Cursor albumCursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }
}
