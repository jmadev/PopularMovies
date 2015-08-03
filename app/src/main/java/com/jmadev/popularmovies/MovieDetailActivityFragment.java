package com.jmadev.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }
    Movie movie;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movie = (Movie) getActivity().getIntent().getSerializableExtra(MainActivityFragment.SER_KEY);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if (movie != null)

        ((TextView) rootView.findViewById(R.id.movie))
                .setText(movie.toString());
        Log.v(LOG_TAG, "Movie poster url : " + movie);


        return rootView;
    }
}
