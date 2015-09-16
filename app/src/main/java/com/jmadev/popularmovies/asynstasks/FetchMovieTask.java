package com.jmadev.popularmovies.asynstasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.adapters.MovieItemAdapter;
import com.jmadev.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    public static final String RESULTS = "results";
    private MovieItemAdapter movieItemAdapter;
    private ArrayList<Movie> mListMovies = new ArrayList<>();

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private Context mContext;

    public FetchMovieTask(Context context, MovieItemAdapter movieItemAdapter) {
        mContext = context;
        this.movieItemAdapter = movieItemAdapter;
    }

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        ArrayList<Movie> movieResults = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(movieObject);
            movie.setBackdropPath(movieObject);
            movie.setOverview(movieObject);
            movie.setReleaseDate(movieObject);
            movie.setPosterPath(movieObject);
            movie.setTitle(movieObject);
            movie.setVoteAverage(movieObject);
            movieResults.add(movie);
            Log.d(LOG_TAG, "Movie: " + movie);
        }
        return movieResults;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JASON response as a string
        String movieJsonStr = null;

        String apiKey = mContext.getString(R.string.api_key);

        try {
            //Construct the UR: for the themovie.db.org query
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create request to themovie.db.org, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a lot easier if you print out the completed
                // buffer for debugging
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movie JSON String: " + url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or passing the movie.
        return null;
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