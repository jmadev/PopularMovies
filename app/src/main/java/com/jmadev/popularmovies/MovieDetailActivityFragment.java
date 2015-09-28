package com.jmadev.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jmadev.popularmovies.adapters.TopCastAdapter;
import com.jmadev.popularmovies.adapters.TrailerAdapter;
import com.jmadev.popularmovies.asynstasks.AddToFavTask;
import com.jmadev.popularmovies.asynstasks.DeleteFromFavTask;
import com.jmadev.popularmovies.asynstasks.FetchCastTask;
import com.jmadev.popularmovies.asynstasks.FetchTrailersTask;
import com.jmadev.popularmovies.models.Cast;
import com.jmadev.popularmovies.models.Movie;
import com.jmadev.popularmovies.models.Trailer;
import com.jmadev.popularmovies.utilities.Util;
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
    CardView cardview_review, cardview_trailer, cardview_cast;
    TextView vote_average, trailer, review, cast;
    TextView movie_release_date;
    TextView overview_info;
    RatingBar ratingBar;
    FloatingActionButton fab;
    public int movieId;
    private TrailerAdapter trailerAdapter;
    private TopCastAdapter topCastAdapter;
    private ArrayList<Trailer> mListTrailers = new ArrayList<>();

    private Toast toast;
    private ArrayList<Cast> mListAllCast = new ArrayList<>();
    private ArrayList<Cast> mListTopCast = new ArrayList<>();

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

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        boolean isFavorited = Util.isFavorited(getActivity(), movie.getId());
        if (isFavorited) {
            fab.setImageResource(R.drawable.ic_action_unfav);
        } else
            fab.setImageResource(R.drawable.ic_action_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FavoritesTask fetchFavoritesTask = new FavoritesTask(getActivity(), movie.getId(), movie);
//                fetchFavoritesTask.execute();
                boolean isFavorited = Util.isFavorited(getActivity(), movie.getId());
                if (isFavorited) {
                    DeleteFromFavTask deleteFromFavTask = new DeleteFromFavTask(getActivity(), movie);
                    deleteFromFavTask.execute();
                    fab.setImageResource(R.drawable.ic_action_fav);
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getActivity(), movie.getTitle() + " removed from Favorites!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    AddToFavTask addToFavTask = new AddToFavTask(getActivity(), movie);
                    addToFavTask.execute();
                    fab.setImageResource(R.drawable.ic_action_unfav);
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getActivity(), movie.getTitle() + " added to Favorites!", Toast.LENGTH_SHORT);
                    toast.show();

                }
                Log.v(LOG_TAG, String.valueOf(isFavorited));
                Log.v(LOG_TAG, String.valueOf(movie.getId()));
            }
        });

        trailer = (TextView) rootView.findViewById(R.id.trailer);
        cardview_trailer = (CardView) rootView.findViewById(R.id.cardview_trailer);
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

        cast = (TextView) rootView.findViewById(R.id.cast);
        cardview_cast = (CardView) rootView.findViewById(R.id.cardview_cast);
        topCastAdapter = new TopCastAdapter(getActivity(), mListTopCast);
        LinearListView castView = (LinearListView) rootView.findViewById(R.id.cast_list);
        castView.setAdapter(topCastAdapter);

        review = (TextView) rootView.findViewById(R.id.review);
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

    private boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isAvailable() && activeNetwork.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasInternetConnection()) {
            runFetchMovieTrailersTask();
            runFetchMovieCastTask();
        } else {
            cast.setVisibility(View.GONE);
            cardview_cast.setVisibility(View.GONE);
            review.setVisibility(View.GONE);
            trailer.setVisibility(View.GONE);
            cardview_review.setVisibility(View.GONE);
            cardview_trailer.setVisibility(View.GONE);
        }

    }
}