package com.os.music.player.models;

import android.database.Cursor;

import com.os.music.player.database.DBManager;

/**
 * Created by hue on 18/05/2017.
 */

public class MySong {
    private String nameSong, nameArist, nameAlbum, genres;
    private String id;
    private int like;
    private String data;
    private String time;
    private String album_id;
    private String albumArtpath;

    public MySong() {

    }

    public MySong(String data, String nameSong, String nameArist, String nameAlbum, String genres, int like, String time, String album_id) {
        this.nameSong = nameSong;
        this.nameArist = nameArist;
        this.nameAlbum = nameAlbum;
        this.genres = genres;
        this.data = data;
        this.id = java.util.UUID.randomUUID().toString();
        this.like = like;
        this.time = time;
        this.album_id = album_id;
    }


    public String getAlbumArtpath() {
        return albumArtpath;
    }

    public void setAlbumArtpath(String albumArtpath) {
        this.albumArtpath = albumArtpath;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MySong(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndex(DBManager.ID));
        this.nameSong = cursor.getString(cursor.getColumnIndex(DBManager.NAME_SONG));
        this.nameArist = cursor.getString(cursor.getColumnIndex(DBManager.NAME_ARIST));
        this.nameAlbum = cursor.getString(cursor.getColumnIndex(DBManager.ALBUMS));
        this.genres = cursor.getString(cursor.getColumnIndex(DBManager.GENRES));
        this.data = cursor.getString(cursor.getColumnIndex(DBManager.DATA));
        this.like = cursor.getInt(cursor.getColumnIndex(DBManager.LIKE));
        this.time = cursor.getString(cursor.getColumnIndex(DBManager.DURATION));
        this.albumArtpath = cursor.getString(cursor.getColumnIndex(DBManager.ALBUM_IMAGE_PATH));
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameArist() {
        return nameArist;
    }

    public void setNameArist(String nameArist) {
        this.nameArist = nameArist;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
