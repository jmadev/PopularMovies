package com.jmadev.popularmovies.asynstasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.jmadev.popularmovies.MainActivityFragment;
import com.jmadev.popularmovies.adapters.MovieItemAdapter;
import com.jmadev.popularmovies.data.MovieContract;
import com.jmadev.popularmovies.models.Movie;

import java.util.ArrayList;


public class FetchFavoritesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private Context mContext;
    private MovieItemAdapter movieItemAdapter;
    private ArrayList<Movie> mListMovies = new ArrayList<>();

    public FetchFavoritesTask(Context context, MovieItemAdapter movieItemAdapter) {
        mContext = context;
        this.movieItemAdapter = movieItemAdapter;
    }

    private ArrayList<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
        ArrayList<Movie> favoritesResults = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor);
                movie.setBackdropPath(cursor);
                movie.setOverview(cursor);
                movie.setReleaseDate(cursor);
                movie.setPosterPath(cursor);
                movie.setTitle(cursor);
                movie.setVoteAverage(cursor);
                favoritesResults.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoritesResults;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MainActivityFragment.MOVIE_COLUMNS,
                null,
                null,
                null
        );
        return getFavoriteMoviesDataFromCursor(cursor);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies != null) {
            if (movieItemAdapter != null)
                movieItemAdapter.setMovies(movies);
            mListMovies = new ArrayList<>();
            mListMovies.addAll(movies);
        }
    }
}

