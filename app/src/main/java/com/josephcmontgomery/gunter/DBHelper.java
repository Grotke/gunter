package com.josephcmontgomery.gunter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joseph on 10/5/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CHANNEL_TABLE =
            "CREATE TABLE " + DatabaseContract.Channel.TABLE_NAME + " (" +
                    DatabaseContract.Channel._ID + INT_PRIMARY_KEY_TYPE + COMMA_SEP +
                    DatabaseContract.Channel.COLUMN_NAME_YOUTUBE_CHANNEL_ID + COMMA_SEP +
                    DatabaseContract.Channel.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Channel.COLUMN_NAME_SUBSCRIBED + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Channel.COLUMN_NAME_CHUNK_STATS + TEXT_TYPE
                    +" )";

    private static final String SQL_CREATE_SERIES_TABLE =
            "CREATE TABLE " + DatabaseContract.Series.TABLE_NAME + " (" +
                    DatabaseContract.Series._ID + INT_PRIMARY_KEY_TYPE + COMMA_SEP +
                    DatabaseContract.Series.COLUMN_NAME_CHANNEL_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Series.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Series.COLUMN_NAME_IGNORED + INTEGER_TYPE
                    + " )";

    private static final String SQL_CREATE_VIDEO_TABLE =
            "CREATE TABLE " + DatabaseContract.Video.TABLE_NAME + " (" +
                    DatabaseContract.Video._ID + INT_PRIMARY_KEY_TYPE + COMMA_SEP +
                    DatabaseContract.Video.COLUMN_NAME_YOUTUBE_VIDEO_ID + COMMA_SEP +
                    DatabaseContract.Video.COLUMN_NAME_CHANNEL_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Video.COLUMN_NAME_SERIES_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Video.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.Video.COLUMN_NAME_VIEWED + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_CHUNK_TABLE =
            "CREATE TABLE " + DatabaseContract.Chunk.TABLE_NAME + " (" +
                    DatabaseContract.Chunk._ID + INT_PRIMARY_KEY_TYPE + COMMA_SEP +
                    DatabaseContract.Chunk.COLUMN_NAME_CHANNEL_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Chunk.COLUMN_NAME_VIDEO_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Chunk.COLUMN_NAME_TITLE + TEXT_TYPE
                    + " )";

    private static final String SQL_DELETE_TABLES =
            "DROP TABLE IF EXISTS " + DatabaseContract.Channel.TABLE_NAME + COMMA_SEP
                    + DatabaseContract.Series.TABLE_NAME + COMMA_SEP
                    + DatabaseContract.Video.TABLE_NAME + COMMA_SEP
                    + DatabaseContract.Chunk.TABLE_NAME;


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Gunter.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_CHANNEL_TABLE);
        db.execSQL(SQL_CREATE_SERIES_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
        db.execSQL(SQL_CREATE_CHUNK_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
