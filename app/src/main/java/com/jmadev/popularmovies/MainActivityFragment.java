package com.jmadev.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String RESULTS = "results";
    private static final String MOVIES_KEY = "state_movies";
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private List<Movie> movies = new ArrayList<>();
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String SORT_BY_SETTINGS_KEY = "";
    private String sortBy = POPULARITY_DESC;
    private ArrayList<Movie> mListMovies = new ArrayList<>();
    private MovieItemAdapter movieItemAdapter;
    public final static String PAR_KEY = "com.jmadev.popularmovies.par";


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);

        switch (sortBy) {
            case POPULARITY_DESC:
                if (!action_sort_by_popularity.isChecked())
                    action_sort_by_popularity.setChecked(true);
                break;
            case RATING_DESC:
                if (!action_sort_by_rating.isChecked())
                    action_sort_by_rating.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sortBy = POPULARITY_DESC;
                updateMovie(sortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sortBy = RATING_DESC;
                updateMovie(sortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieItemAdapter = new MovieItemAdapter(getActivity(), movies);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieItemAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PAR_KEY, movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_BY_SETTINGS_KEY)) {
                sortBy = savedInstanceState.getString(SORT_BY_SETTINGS_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mListMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                movieItemAdapter.setMovies(mListMovies);
            } else {
                updateMovie(sortBy);
            }
        } else {
            updateMovie(sortBy);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!sortBy.contentEquals(POPULARITY_DESC))
            outState.putString(SORT_BY_SETTINGS_KEY, sortBy);
        if (mListMovies != null)
            outState.putParcelableArrayList(MOVIES_KEY, mListMovies);
        super.onSaveInstanceState(outState);
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected()) {
            return true;
        } else
            Toast.makeText(getActivity(), "No Internet connection, please check connectivity!",
                    Toast.LENGTH_LONG).show();
        return false;
    }


    private void updateMovie(String sortBy) {
        if (hasInternetConnection()) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(sortBy);
        }
    }


    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private List<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);

            List<Movie> movieResults = new ArrayList<>();
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

                }
            return movieResults;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JASON response as a string
            String movieJsonStr = null;

            String apiKey = getString(R.string.api_key);

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
        protected void onPostExecute(List<Movie> movies) {

            if (movies != null) {
                if (movieItemAdapter != null)
                    movieItemAdapter.setMovies(movies);
                mListMovies = new ArrayList<>();
                mListMovies.addAll(movies);
                Log.v(LOG_TAG, "MOVIES" + movies);
            }

        }
    }


}
