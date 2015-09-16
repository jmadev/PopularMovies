package com.jmadev.popularmovies.asynstasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.adapters.TrailerAdapter;
import com.jmadev.popularmovies.models.Trailer;

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

public class FetchMovieTrailersTask extends AsyncTask<Void, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchMovieTrailersTask.class.getSimpleName();
    public static final String YOUTUBE = "youtube";
    public static final String TRAILERS = "trailers";
    private ArrayList<Trailer> mListTrailers = new ArrayList<>();

    private Context mContext;
    private int movieId;
    private TrailerAdapter trailerAdapter;

    public FetchMovieTrailersTask(Context context, TrailerAdapter trailerAdapter, int movieId) {
        mContext = context;
        this.trailerAdapter = trailerAdapter;
        this.movieId = movieId;
    }
    private ArrayList<Trailer> getMovieTrailerFromJson(String trailerJsonStr)
            throws JSONException {

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONObject youtubeJson = (JSONObject) trailerJson.get(TRAILERS);
        JSONArray trailerArray = youtubeJson.getJSONArray(YOUTUBE);

        ArrayList<Trailer> trailerResults = new ArrayList<>();
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailerObject = trailerArray.getJSONObject(i);
            Trailer trailer = new Trailer();
            trailer.setName(trailerObject);
            trailer.setSource(trailerObject);
            trailerResults.add(trailer);
            Log.d(LOG_TAG, "Trailers: " + trailer);
        }
        return trailerResults;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JASON response as a string
        String movieJsonStr = null;

        String apiKey = mContext.getString(R.string.api_key);
        Log.v(LOG_TAG, String.valueOf(movieId));
        String _ID = Integer.toString(movieId);
        String creditsTrailersReviews = "credits,trailers,reviews";

        try {
            //Construct the URL for the themovie.db.query
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + _ID + "?";
            final String API_KEY_PARAM = "api_key";
            final String appendCreditsReviewsTrailers = "append_to_response";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                    .appendQueryParameter(appendCreditsReviewsTrailers, creditsTrailersReviews)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI: " + builtUri.toString());

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

            Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);
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
            return getMovieTrailerFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or passing the movie.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        if( trailers != null) {
            if(trailerAdapter != null) {
                trailerAdapter.setTrailers(trailers);
            }
            mListTrailers.addAll(trailers);
        }
    }
}