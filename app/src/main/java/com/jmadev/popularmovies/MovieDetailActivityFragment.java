package com.jmadev.popularmovies;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
        ImageView mImageView = null;

        CollapsingToolbarLayout collapsingToolbarLayout = null;

        movie = (Movie) getActivity().getIntent().getSerializableExtra(MainActivityFragment.SER_KEY);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (movie != null)
            collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        mImageView = (ImageView) rootView.findViewById(R.id.movie_backdrop);
        //collapsingToolba
        // rLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.movie);
        Log.v(LOG_TAG, "Movie poster url : " + movie);
        collapsingToolbarLayout.setTitle(movie.getTitle());
        String url = movie.getBackdropPath();

        Glide.with(getActivity())
                .load(url)
                        .centerCrop()
                //.fitCenter()
                .crossFade()
                .into(mImageView);

        return rootView;
    }
}
