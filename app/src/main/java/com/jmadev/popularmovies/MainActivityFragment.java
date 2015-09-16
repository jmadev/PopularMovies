package com.jmadev.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jmadev.popularmovies.adapters.MovieItemAdapter;
import com.jmadev.popularmovies.asynstasks.FetchMovieTask;
import com.jmadev.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  {


    private static final String MOVIES_KEY = "state_movies";
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private List<Movie> movies = new ArrayList<>();
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String SORT_BY_SETTINGS_KEY = "";
    private static String sortBy = POPULARITY_DESC;
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
            FetchMovieTask movieTask = new FetchMovieTask(getActivity(), movieItemAdapter);
            movieTask.execute(sortBy);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie(sortBy);
    }
}