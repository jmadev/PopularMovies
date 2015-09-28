package com.jmadev.popularmovies.utilities;

import android.content.Context;
import android.database.Cursor;

import com.jmadev.popularmovies.data.MovieContract;

public class Util {
    private final String LOG_TAG = Util.class.getSimpleName();

    public static boolean isFavorited(Context context, int movieId) {
        boolean boolIsFavorited = false;
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COL_ID},
                MovieContract.MovieEntry.COL_ID + " = ?",
                new String[]{Integer.toString(movieId)},
                null
        );

        if(cursor.moveToFirst()) {
            boolIsFavorited = true;
        }
        cursor.close();
        return boolIsFavorited;
    }
}
