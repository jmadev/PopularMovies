package com.jmadev.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

    private MovieItemAdapter movieItemAdapter;

    MovieItem[] movieItems = {
            new MovieItem("http://www.freedesign4.me/wp-content/gallery/posters/free-movie-film-poster-the_dark_knight_movie_poster.jpg"),
            new MovieItem("http://gdj.gdj.netdna-cdn.com/wp-content/uploads/2011/12/grey-movie-poster.jpg"),
            new MovieItem("http://t0.gstatic.com/images?q=tbn:ANd9GcSZpU0mfaphHfdRHAAGxbPCh8UlLn6jYL52a-YYFRUHZPt9MFZK"),
            new MovieItem("http://www.fatmovieguy.com/wp-content/uploads/2013/09/Don-Jon-Movie-Poster.jpg"),
            new MovieItem("http://webneel.com/daily/sites/default/files/images/daily/02-2013/24-silence-of-the-lambs-creative-movie-poster-design.jpg")
    };

    List<MovieItem> movieItems2 = new ArrayList<>();

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

//       movieItemAdapter = new MovieItemAdapter(getActivity(), Arrays.asList(movieItems));
        movieItemAdapter = new MovieItemAdapter(getActivity(),movieItems2);


        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(movieItemAdapter);

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

    public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private String[] getMovieDataFromJson(String movieJsonStr, int numMovies)
                throws JSONException {

            // base URL to fetch image
            final String posterBasePathUrl = "http://image.tmdb.org/t/p/w185//";

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieItemArray = movieJson.getJSONArray(TMDB_RESULTS);

            String[] resultStrs = new String[numMovies];

            for (int i = 0; i < movieItemArray.length(); i++) {
                String moviePosterURL;

                // Get the JSON object representing the movie
                //JSONObject movieName = movieItemArray.getJSONObject(i);

                // Get the JSON object representing movie poster
                JSONObject moviePosterObject = movieItemArray.getJSONObject(i);
                moviePosterURL = moviePosterObject.getString(TMDB_POSTER_PATH);

                resultStrs[i] = posterBasePathUrl + moviePosterURL;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Movie entry: " + s);
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(Void... params) {

//            // Verify size of params
//            if (params.length == 0) {
//                return null;
//            }

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
                final String MOVE_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVE_BASE_URL).buildUpon()
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
        protected void onPostExecute(String[] strings) {
            if(strings != null) {
                movieItemAdapter.clear();
                for(String moviePosterPath : strings) {
                    MovieItem mItem = new MovieItem(moviePosterPath);
                    movieItems2.add(mItem);

                }
            }
        }
    }
}
