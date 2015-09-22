package com.jmadev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jmadev.popularmovies.adapters.ReviewAdapter;
import com.jmadev.popularmovies.adapters.TopCastAdapter;
import com.jmadev.popularmovies.adapters.TrailerAdapter;
import com.jmadev.popularmovies.asynstasks.FetchCastTask;
import com.jmadev.popularmovies.asynstasks.FetchTrailersTask;
import com.jmadev.popularmovies.models.Cast;
import com.jmadev.popularmovies.models.Movie;
import com.jmadev.popularmovies.models.Review;
import com.jmadev.popularmovies.models.Trailer;
import com.linearlistview.LinearListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();


    ImageView movie_poster_image;
    KenBurnsView movie_backdrop;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button mButton;
    Movie movie;
    Cast cast;
    CardView cardview_review;
    TextView vote_average;
    TextView movie_release_date;
    TextView overview_info;
    RatingBar ratingBar;
    public int movieId;
    private TrailerAdapter trailerAdapter;
    private TopCastAdapter topCastAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Trailer> mListTrailers = new ArrayList<>();
    private ArrayList<Review> mListAllReviews = new ArrayList<>();


    private ArrayList<Cast> mListAllCast = new ArrayList<Cast>();
    private ArrayList<Cast> mListTopCast = new ArrayList<>();
    public static final String PAR_KEY = "com.jmadev.popularmovies.cast.par";

    public MovieDetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null)
            movieId = getArguments().getInt("movieId");



        movie = getActivity().getIntent().getParcelableExtra(MainActivityFragment.PAR_KEY);
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mButton = (Button) rootView.findViewById(R.id.btn_all_cast);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AllCastActivity.class);
                intent.putParcelableArrayListExtra("castList", mListAllCast);
                v.getContext().startActivity(intent);
            }
        });

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

        topCastAdapter = new TopCastAdapter(getActivity(), mListTopCast);
        LinearListView castView = (LinearListView) rootView.findViewById(R.id.cast_list);
        castView.setAdapter(topCastAdapter);

        cardview_review = (CardView) rootView.findViewById(R.id.cardview_reviews);
        cardview_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReviewActivity.class);
                intent.putExtra("movieid", movie.getId());
                v.getContext().startActivity(intent);
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



        return rootView;
    }

    public void runFetchMovieTrailersTask() {
        FetchTrailersTask task = new FetchTrailersTask(getActivity(), trailerAdapter, movie.getId());
        task.execute();
    }
    public void runFetchMovieCastTask() {
        FetchCastTask castTask = new FetchCastTask(getActivity(), topCastAdapter, movie.getId(), mListAllCast);
        castTask.execute();
    }




    @Override
    public void onStart() {
        super.onStart();
        runFetchMovieTrailersTask();
        runFetchMovieCastTask();
    }
}