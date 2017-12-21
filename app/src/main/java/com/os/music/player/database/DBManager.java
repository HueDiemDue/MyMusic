package com.os.music.player.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.os.music.player.models.Albums;
import com.os.music.player.models.MySong;
import com.os.music.player.utils.Constants;
import com.os.music.player.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hue on 18/05/2017.
 */

public enum DBManager {
    INSTANCE;
    public static final String TABLE_MUSIC = "TABLE_MUSIC";
    public static final String TABLE_ALBUMS = "TABLE_ALBUMS";

    public static final String ID = "ID";
    public static final String NAME_SONG = "NAME_SONG";
    public static final String NAME_ARIST = "NAME_ARIST";
    public static final String DATA = "DATA";
    public static final String LIKE = "LIKE";
    public static final String ALBUMS = "ALBUMS";
    public static final String ALBUMS_ID = "ALBUMS_ID";
    public static final String GENRES = "GENRES";
    public static final String DURATION = "DURATIONS";
    public static final String ALBUM_IMAGE_PATH = "ALBUM_IMAGE_PATH";

    public static String DB_PATH = "";
    public static String DB_NAME = "Music.db";
    public static final int DB_VERSION = 1;
    private DatabaseHelper databaseHelper;


    public static String create_table_music = " CREATE TABLE IF NOT EXISTS " + TABLE_MUSIC + "( "
            + ID + " TEXT primary key, "
            + NAME_SONG + " TEXT, "
            + NAME_ARIST + " TEXT, "
            + DATA + " TEXT, "
            + ALBUMS + " TEXT, "
            + GENRES + " TEXT, "
            + ALBUMS_ID + " TEXT, "
            + DURATION + " TEXT, "
            + LIKE + " INTEGER, "
            + ALBUM_IMAGE_PATH + " TEXT)";

    public static String create_table_albums = " CREATE TABLE IF NOT EXISTS " + TABLE_ALBUMS + "( "
            + ID + " TEXT primary key, "
            + NAME_SONG + " TEXT)";
//    // init dâtbase

    // manager
    // 1. getAllSong
    public ArrayList<MySong> getAllSong() {
        ArrayList<MySong> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MySong mySong = new MySong(cursor);
                list.add(mySong);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // lay ra tat ca cac albums
    public ArrayList<String> getAllSongByAlbums() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MySong mySong = new MySong(cursor);
                list.add(mySong.getNameAlbum());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getAllPath() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                MySong mySong = new MySong(cursor);
                list.add(mySong.getData());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // name albums có => false, else true
    public boolean check_NameAlbum(String nameAlbum, ArrayList<Albums> list) {
        if (list.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNameAlbums().equals(nameAlbum)) {
                    return false;
                }
            }
            return true;
        }
    }

    // xoa cac albums co ten giong nhau roi add vao list albums
    public ArrayList<Albums> getListAlbums() {
        ArrayList<Albums> albumsArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (check_NameAlbum(cursor.getString(4), albumsArrayList)) {
                    albumsArrayList.add(new Albums(cursor.getString(6), cursor.getString(4)));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return albumsArrayList;
    }

    // lay ra tat ca cac albums
    public ArrayList<MySong> getAllSongByAlbum(String albums) {
        ArrayList<MySong> listSong = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (albums.equals(cursor.getString(4))) {
                    MySong mySong = new MySong(cursor);
                    listSong.add(mySong);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listSong;
    }

    // get song name from id
    public String getPathSongByID(String ID) {
        String s = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(0).equals(ID)) {
                    s = cursor.getString(3);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return s;
    }

    public ArrayList<MySong> getAllSongByArist(String arist) {
        ArrayList<MySong> listSong = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                if (arist.equals(cursor.getString(2))) {
                    MySong mySong = new MySong(cursor);
                    listSong.add(mySong);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listSong;
    }

    public ArrayList<MySong> getAllSongByLike() {
        ArrayList<MySong> listSong = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (1 == cursor.getInt(8)) {
                    MySong mySong = new MySong(cursor);
                    listSong.add(mySong);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listSong;
    }

    // name albums có => false, else true
    public boolean check_NameArist(String nameArist, ArrayList<String> list) {
        if (list.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(nameArist)) {
                    return false;
                }
            }
            return true;
        }
    }

    // xoa cac albums co ten giong nhau roi add vao list albums
    public ArrayList<String> getListArist() {
        ArrayList<String> aristArrayList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (check_NameArist(cursor.getString(2), aristArrayList)) {
                    aristArrayList.add(cursor.getString(2));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return aristArrayList;
    }

    public int getSizeData() {
        ArrayList<MySong> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        int i = 0;
        Cursor cursor = db.rawQuery(query, null);
        i = cursor.getCount();

        cursor.close();
        return i;
    }

    public boolean checkDatabaseNull() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            // co
            return false;
        } else {
            // khong
            return true;
        }
    }

    // insert in data
    public void insertSong(ArrayList<MySong> list, Context context) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();

        if (!checkDatabaseNull()) {
            db.execSQL("DELETE FROM " + TABLE_MUSIC);
        }
        for (int i = 0; i < list.size(); i++) {
            ContentValues cValues = new ContentValues();
            cValues.put(ID, list.get(i).getId());
            cValues.put(NAME_SONG, list.get(i).getNameSong());
            cValues.put(NAME_ARIST, list.get(i).getNameArist());
            cValues.put(DATA, list.get(i).getData());
            cValues.put(LIKE, list.get(i).getLike());
            cValues.put(ALBUMS, list.get(i).getNameAlbum());
            cValues.put(GENRES, list.get(i).getGenres());
            cValues.put(DURATION, list.get(i).getTime() + "");
            cValues.put(ALBUMS_ID, list.get(i).getAlbum_id());
            cValues.put(ALBUM_IMAGE_PATH, Utils.getCoverArtPath(Long.parseLong(list.get(i).getAlbum_id()), context));
            db.insert(TABLE_MUSIC, null, cValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        context.sendBroadcast(new Intent("loading.data"));
    }

    public MySong getSong(String id) {
        MySong mSong = new MySong();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC + " WHERE ID ='" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                mSong = new MySong(cursor);
                cursor.close();
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return mSong;
    }

    public String getIdSong(String path) {
        String id = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                if (cursor.getString(3).equals(path)) {
                    id = cursor.getString(0);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return id;
    }

    public String getArtAlbumPath(String id) {
        String path = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                if (cursor.getString(0).equals(id)) {
                    path = cursor.getString(9);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return path;
    }

    public String getNextPath(String id) {
        boolean b = false;
        String path = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (!id.equals(getLastId())) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    if (b) {
                        path = cursor.getString(3);
                        b = false;
                    } else {
                        if (cursor.getString(0).equals(id)) {
                            b = true;
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            path = getFirstPath();
        }

        return path;
    }

    public String getPrevPath(String id) {
        boolean b = false;
        String path = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (!id.equals(getFirstId())) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                do {
                    if (b) {
                        path = cursor.getString(3);
                        b = false;
                    } else {
                        if (cursor.getString(0).equals(id)) {
                            b = true;
                        }
                    }
                } while (cursor.moveToPrevious());
            }
            cursor.close();
        } else {
            path = getLastPath();
        }
        return path;
    }

    public String getFirstId() {
        String firstId = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            firstId = cursor.getString(0);
        }
        cursor.close();
        return firstId;
    }

    public String getFirstPath() {
        String firstPath = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            firstPath = cursor.getString(3);
        }
        cursor.close();
        return firstPath;
    }

    public String getLastId() {
        String lastId = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            lastId = cursor.getString(0);
        }
        cursor.close();
        return lastId;
    }

    public String getLastPath() {
        String lastPath = "";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            lastPath = cursor.getString(3);
        }
        cursor.close();
        return lastPath;
    }

    public String getNextPathAlbums(String path, String albums) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByAlbum(albums);
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == listSong.size() - 1) {
                    return listSong.get(0).getData();
                } else {
                    return listSong.get(i + 1).getData();
                }

            }
        }
        return "";
    }

    public String getPrevPathAlbums(String path, String albums) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByAlbum(albums);
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == 0) {
                    return listSong.get(listSong.size() - 1).getData();
                } else {
                    return listSong.get(i - 1).getData();
                }

            }
        }
        return "";
    }

    public String getNextPathArtist(String path, String artist) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByArist(artist);
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == listSong.size() - 1) {
                    return listSong.get(0).getData();
                } else {
                    return listSong.get(i + 1).getData();
                }

            }
        }
        return "";
    }

    public String getNextPathLike(String path) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByLike();
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == listSong.size() - 1) {
                    return listSong.get(0).getData();
                } else {
                    return listSong.get(i + 1).getData();
                }

            }
        }
        return "";
    }

    public String getPrevPathLike(String path) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByLike();
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == 0) {
                    return listSong.get(listSong.size() - 1).getData();
                } else {
                    return listSong.get(i - 1).getData();
                }

            }
        }
        return "";
    }

    public String getPrevPathArtist(String path, String artist) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByArist(artist);
        if (listSong.size() == 1) return listSong.get(0).getData();
        for (int i = 0; i < listSong.size(); i++) {
            if (path.equals(listSong.get(i).getData())) {
                if (i == 0) {
                    return listSong.get(listSong.size() - 1).getData();
                } else {
                    return listSong.get(i - 1).getData();
                }

            }
        }
        return "";
    }

    public String getRandomPathArtist(String artist) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByArist(artist);
        if (listSong.size() == 1) {
            return listSong.get(0).getData();
        } else {
            Random r = new Random();
            int i = r.nextInt(listSong.size());
            return listSong.get(i).getData();
        }

    }

    public String getRandomPathLike() {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByLike();
        if (listSong.size() == 1) {
            return listSong.get(0).getData();
        } else {
            Random r = new Random();
            int i = r.nextInt(listSong.size());
            return listSong.get(i).getData();
        }

    }

    public String getRandomPathAlbums(String albums) {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSongByAlbum(albums);
        if (listSong.size() == 1) {
            return listSong.get(0).getData();
        } else {
            Random r = new Random();
            int i = r.nextInt(listSong.size());
            return listSong.get(i).getData();
        }

    }

    public String getRandomPath() {
        ArrayList<MySong> listSong = DBManager.INSTANCE.getAllSong();
        if (listSong.size() == 1) {
            return listSong.get(0).getData();
        } else {
            int random = (int) (Math.random() * listSong.size());
//            Random r = new Random();
//            int i = r.nextInt(listSong.size());
            return listSong.get(random).getData();
        }
    }

    public void updateLikeMusic(Context context, String id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ContentValues contentValues = new ContentValues();
                contentValues.put(LIKE, 1);
                db.update(TABLE_MUSIC, contentValues, ID + "='" + id + "'", null);
            } while (cursor.moveToNext());

        }
        cursor.close();
        context.sendBroadcast(new Intent(Constants.Intents.UPDATE_LIKED));
    }

    public void updateDislikeMusic(Context context, String id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ContentValues contentValues = new ContentValues();
                contentValues.put(LIKE, 0);
                db.update(TABLE_MUSIC, contentValues, ID + "='" + id + "'", null);
            } while (cursor.moveToNext());
        }
        cursor.close();
        context.sendBroadcast(new Intent(Constants.Intents.UPDATE_LIKED));
    }

    public boolean checkLiked(String id) {
        boolean beLiked = false;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MUSIC;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(0).equals(id)) {
                    if (cursor.getInt(8) == 0) {
                        beLiked = false;
                    } else {
                        beLiked = true;
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return beLiked;
    }


    /**
     * khởi tạo database
     *
     * @param context
     */
    public static void init(Context context) {
        if (INSTANCE.databaseHelper == null) {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            File file = new File(DB_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            INSTANCE.databaseHelper = new DatabaseHelper(context);
            try {
                INSTANCE.databaseHelper.createDataBase();
                INSTANCE.databaseHelper.openDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private SQLiteDatabase sqLiteDatabase;

        public DatabaseHelper(Context context) {
            super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        }

        public void createDataBase() throws IOException {
            this.getReadableDatabase();
        }

        public void openDataBase() throws SQLException {
            close();
            // Open the database
            String myPath = DB_PATH + DB_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(create_table_music);
//                db.execSQL(create_table_albums);

            } catch (android.database.SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);
            onCreate(sqLiteDatabase);
        }

        @Override
        public synchronized void close() {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
            super.close();
        }
    }
}
