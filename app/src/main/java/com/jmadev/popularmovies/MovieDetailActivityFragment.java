package com.jmadev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.linearlistview.LinearListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    public static final String YOUTUBE = "youtube";
    public static final String TRAILERS = "trailers";
    ImageView movie_poster_image;
    KenBurnsView movie_backdrop;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Movie movie;
    TextView vote_average;
    TextView movie_release_date;
    TextView overview_info;
    RatingBar ratingBar;
    public int movieId;
    private TrailerAdapter trailerAdapter;
    private ArrayList<Trailer> mListTrailers = new ArrayList<>();

    public MovieDetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null)
            movieId = getArguments().getInt("movieId");

        movie = getActivity().getIntent().getParcelableExtra(MainActivityFragment.PAR_KEY);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        trailerAdapter = new TrailerAdapter(getActivity(), mListTrailers);
        LinearListView trailersView = (LinearListView) rootView.findViewById(R.id.trailer_youtube);
        trailersView.setAdapter(trailerAdapter);
        trailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view, int position, long id) {
                Trailer trailer = trailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getSource()));
                        startActivity(intent);
            }
        });

        if (movie != null) {
            collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
            movie_backdrop = (KenBurnsView) rootView.findViewById(R.id.movie_backdrop);
            movie_poster_image = (ImageView) rootView.findViewById(R.id.movie_poster_image);
            vote_average = (TextView) rootView.findViewById(R.id.vote_average);
            ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
            movie_release_date = (TextView) rootView.findViewById(R.id.movie_release_date);
            overview_info = (TextView) rootView.findViewById(R.id.overview_info);
            vote_average.setText(Double.toString(movie.getVoteAverage()));
            collapsingToolbarLayout.setTitle(movie.getTitle());
            String movieBackdropPathURL = movie.getBackdropPath();
            String moviePosterURL = movie.getPosterPath();
            String oldReleaseDate = movie.getReleaseDate();
            overview_info.setText(movie.getOverview());
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldReleaseDate);
                String newReleaseDate = new SimpleDateFormat("MM/dd/yyy").format(date);
                movie_release_date.setText(newReleaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ratingBar.setRating((float) movie.getVoteAverage());
            Glide.with(getActivity())
                    .load(movieBackdropPathURL)
                    .centerCrop()
                    .crossFade()
                    .into(movie_backdrop);

            Glide.with(getActivity())
                    .load(moviePosterURL)
                    .fitCenter()
                    .crossFade()
                    .into(movie_poster_image);
        }
        FetchMovieTrailersTask task = new FetchMovieTrailersTask();
        task.execute();
        return rootView;
    }

    public class FetchMovieTrailersTask extends AsyncTask<Integer, Void, ArrayList<Trailer>> {

        private final String LOG_TAG = FetchMovieTrailersTask.class.getSimpleName();

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
        protected ArrayList<Trailer> doInBackground(Integer... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JASON response as a string
            String trailerJsonStr = null;

            String apiKey = getString(R.string.api_key);
            String _ID = Integer.toString(movie.getId());
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
                trailerJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movie JSON String: " + trailerJsonStr);
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
                return getMovieTrailerFromJson(trailerJsonStr);
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
}