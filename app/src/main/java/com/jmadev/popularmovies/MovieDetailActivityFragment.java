package com.jmadev.popularmovies;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    ImageView  movie_poster_image;
    KenBurnsView movie_backdrop;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Movie movie;
    TextView vote_average;
    TextView movie_release_date;
    TextView overview_info;
    RatingBar ratingBar;

    public MovieDetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movie = getActivity().getIntent().getParcelableExtra(MainActivityFragment.PAR_KEY);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

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

        return rootView;
    }
}