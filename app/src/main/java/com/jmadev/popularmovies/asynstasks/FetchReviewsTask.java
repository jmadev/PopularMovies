package com.jmadev.popularmovies.asynstasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.adapters.ReviewAdapter;
import com.jmadev.popularmovies.models.Review;

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
import java.util.List;

public class FetchReviewsTask extends AsyncTask<Void, Void, ArrayList<Review>> {

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    public static final String RESULTS = "results";
    public static final String REVIEWS = "reviews";
    public List<Review> mListReviews = new ArrayList<>();

    private Context mContext;
    private int movieId;
    private ReviewAdapter reviewAdapter;
    private RecyclerView rv;

    public FetchReviewsTask(Context context, ReviewAdapter reviewAdapter, int movieId, List<Review> reviewList, RecyclerView recyclerView) {
        mContext = context;
        this.reviewAdapter = reviewAdapter;
        this.movieId = movieId;
        this.mListReviews = reviewList;
        this.rv = recyclerView;
    }

    private ArrayList<Review> getMovieReviewsFromJson(String reviewJsonStr)
            throws JSONException {

        JSONObject reviewJson = new JSONObject(reviewJsonStr);
        JSONObject resultsJson = (JSONObject) reviewJson.get(REVIEWS);
        JSONArray reviewArray = resultsJson.getJSONArray(RESULTS);

        ArrayList<Review> reviewResults = new ArrayList<>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject trailerObject = reviewArray.getJSONObject(i);
            Review review = new Review();
            review.setAuthor(trailerObject);
            review.setContent(trailerObject);
            reviewResults.add(review);
            Log.d(LOG_TAG, "Trailers: " + review);
        }
        return reviewResults;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... params) {
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
            return getMovieReviewsFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or passing the movie.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        if (reviews.size() == 0) {
            Toast.makeText(mContext, "No reviews for this movie!", Toast.LENGTH_LONG).show();
        } else {
            reviewAdapter = new ReviewAdapter(mContext, reviews);
            rv.setAdapter(reviewAdapter);

        }
    }
}