package com.os.music.player.models;

import android.database.Cursor;

import com.os.music.player.database.DBManager;

/**
 * Created by hue on 19/05/2017.
 */

public class Albums {
    private String id;
    private String nameAlbums;

    public Albums(String id,String nameAlbums) {
        this.id = id;
        this.nameAlbums = nameAlbums;
    }
    public Albums(){

    }
    public Albums(Cursor cursor){
        this.id = cursor.getString(cursor.getColumnIndex(DBManager.ALBUMS_ID));
        this.nameAlbums = cursor.getString(cursor.getColumnIndex(DBManager.ALBUMS));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAlbums() {
        return nameAlbums;
    }

    public void setNameAlbums(String nameAlbums) {
        this.nameAlbums = nameAlbums;
    }
}
