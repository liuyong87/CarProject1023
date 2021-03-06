package com.smk.autoradio.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smk.autoradio.utils.Logutil;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = Logutil.makeTagLog(DBHelper.class);

    private static final String CREATE_SEARCH_CHANNEL_TABLE = "create table "
            + DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL + "("
            + DBConfiguration.ChannelConfiguration._ID
            + " integer primary key autoincrement ,"
            + DBConfiguration.ChannelConfiguration._CAHNNEL + " integer ,"
            + DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " integer ,"
            + DBConfiguration.ChannelConfiguration._SIGNAL + " integer ,"
            + DBConfiguration.ChannelConfiguration._IS_FAVORITE + " integer "
            + ")";

    private static final String CREATE_FAVORITE_CHANNEL_TABLE = "create table "
            + DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL + "("
            + DBConfiguration.ChannelConfiguration._ID
            + " integer primary key autoincrement ,"
            + DBConfiguration.ChannelConfiguration._CAHNNEL + " integer ,"
            + DBConfiguration.ChannelConfiguration._CAHNNEL_TYPE + " integer ,"
            + DBConfiguration.ChannelConfiguration._SIGNAL + " integer ,"
            + DBConfiguration.ChannelConfiguration._IS_FAVORITE + " integer "
            + ")";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logutil.i(TAG, "=======onCreate()");
        db.execSQL(CREATE_SEARCH_CHANNEL_TABLE);
        db.execSQL(CREATE_FAVORITE_CHANNEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logutil.i(TAG, "=======onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL);
        db.execSQL("DROP TABLE IF EXISTS " + DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logutil.i(TAG, "=======onDowngrade()");
        db.execSQL("DROP TABLE IF EXISTS " + DBConfiguration.ChannelConfiguration.TABLE_NAME_FULL_SEARCH_CHANNEL);
        db.execSQL("DROP TABLE IF EXISTS " + DBConfiguration.ChannelConfiguration.TABLE_NAME_FAVORITE_CHANNEL);
    }
}
