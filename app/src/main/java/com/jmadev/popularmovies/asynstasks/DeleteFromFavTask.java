package com.jmadev.popularmovies.asynstasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jmadev.popularmovies.data.MovieContract;
import com.jmadev.popularmovies.models.Movie;

public class DeleteFromFavTask extends AsyncTask<Void, Void, Integer> {

    private final String LOG_TAG = DeleteFromFavTask.class.getSimpleName();
    private Context mContext;
    private Movie mMovie;

    public DeleteFromFavTask(Context context, Movie movie) {
        mContext = context;
        mMovie = movie;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        return mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COL_ID + " = ?",
                new String[]{String.valueOf(mMovie.getId())}
        );
    }
}
