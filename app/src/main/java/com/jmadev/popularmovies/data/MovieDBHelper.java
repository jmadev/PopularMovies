package com.jmadev.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jmadev.popularmovies.data.MovieContract.MovieEntry;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COL_ID + " INTEGER NOT NULL, " +
                MovieEntry.COL_TITLE + " TEXT NOT NULL," +
                MovieEntry.COL_POSTER_PATH + " TEXT," +
                MovieEntry.COL_OVERVIEW + " TEXT," +
                MovieEntry.COL_RELEASE_DATE + " REAL NOT NULL," +
                MovieEntry.COL_BACKDROP_PATH + " TEXT," +
                MovieEntry.COL_VOTE_AVERAGE + " REAL);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
