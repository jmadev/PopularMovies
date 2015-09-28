package com.jmadev.popularmovies.asynstasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.jmadev.popularmovies.data.MovieContract;
import com.jmadev.popularmovies.models.Movie;

public class AddToFavTask extends AsyncTask<Void, Void, Uri> {

    private Context mContext;
    private Movie mMovie;

    public AddToFavTask(Context context, Movie movie) {
        mContext = context;
        mMovie = movie;
    }

    @Override
    protected Uri doInBackground(Void... params) {
        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COL_ID, mMovie.getId());
        cv.put(MovieContract.MovieEntry.COL_TITLE, mMovie.getTitle());
        cv.put(MovieContract.MovieEntry.COL_POSTER_PATH, mMovie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COL_OVERVIEW, mMovie.getOverview());
        cv.put(MovieContract.MovieEntry.COL_RELEASE_DATE, mMovie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COL_BACKDROP_PATH, mMovie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COL_VOTE_AVERAGE, mMovie.getVoteAverage());

        return mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
    }
}
