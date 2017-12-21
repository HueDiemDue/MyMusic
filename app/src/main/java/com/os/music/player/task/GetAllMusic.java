package com.os.music.player.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import com.os.music.player.database.DBManager;
import com.os.music.player.models.MySong;

/**
 * Created by hue on 19/05/2017.
 */

public class GetAllMusic extends AsyncTask<Void, Void, ArrayList<MySong>> {
    Context context;
    ArrayList<MySong> arrMySong = new ArrayList<>();

    ContentResolver contentResolver;
    Uri songUri;
    String selection, sortOrder;

    public GetAllMusic(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contentResolver = context.getContentResolver();
        songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
    }

    @Override
    protected ArrayList<MySong> doInBackground(Void... params) {
        Cursor cursor = contentResolver.query(songUri, null, selection, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            int mySong = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int singer = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int time = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int album_id = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            Log.d("albums_id",album_id+"");
            do {
                String mSong = cursor.getString(mySong);
                String mSinger = cursor.getString(singer);
                String mData = cursor.getString(data);
                String albums = cursor.getString(album);
                String albums_ids = cursor.getString(album_id);
                int mTime = cursor.getInt(time);

                arrMySong.add(new MySong(mData, mSong, mSinger, albums, "", 0, mTime + "",albums_ids));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrMySong;
    }

    @Override
    protected void onPostExecute(ArrayList<MySong> mySongs) {
        super.onPostExecute(mySongs);
        DBManager.INSTANCE.insertSong(mySongs, context);
    }
}
