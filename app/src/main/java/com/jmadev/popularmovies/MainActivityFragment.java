package com.jmadev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

    private MovieItemAdapter movieItemAdapter;
    private List<MovieInfo> movieInfo;
    public final static String SER_KEY = "com.jmadev.popularmovies.ser";
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<Movie> movies = new ArrayList<>();

//       movieItemAdapter = new MovieItemAdapter(getActivity(), Arrays.asList(movieItems));
        movieItemAdapter = new MovieItemAdapter(getActivity(), movies);


        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieItemAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
//                        .putExtra("movie", movie);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SER_KEY, movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if(movies == null) {
            updateMovie();
        }
        return rootView;
    }

    private void updateMovie() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, List<Movie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private List<Movie> getMovieDataFromJson(String movieJsonStr, int numMovies)
                throws JSONException {

            // base URL to fetch image
            final String posterBasePathUrl = "http://image.tmdb.org/t/p/w185//";

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);

            MovieInfo movieInfo = new MovieInfo(movieJson.toString());
            List<Movie> movies = new ArrayList<>();
            movies.addAll(movieInfo.getMovies());
            //String url = movieInfo.getPosterPath(movieJson);
            Log.v(LOG_TAG, "Movie poster url : " + movies);

//            for (String s : resultStrs) {
//                Log.v(LOG_TAG, "Movie entry: " + s);
//            }
            return movies;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JASON response as a string
            String movieJsonStr = null;

            String sortByPopularity = "popularity.desc";
            String apiKey = getString(R.string.api_key);
            int numMovies = 20;

            try {
                //Construct the UR: for the themovie.db.org query
                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortByPopularity)
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
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null) {
                    // Nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier if you print out the completed
                    // buffer for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error" , e);
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
                return getMovieDataFromJson(movieJsonStr, numMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or pasing the movie.
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies != null) {
                movieItemAdapter.clear();
                for(Movie movie : movies) {
                    movieItemAdapter.add(movie);
                    Log.v(LOG_TAG, "Movies: " + movie);

                }

            }
        }
    }
}
