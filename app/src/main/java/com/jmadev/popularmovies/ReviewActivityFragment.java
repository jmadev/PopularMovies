package com.jmadev.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmadev.popularmovies.adapters.ReviewAdapter;
import com.jmadev.popularmovies.asynstasks.FetchReviewsTask;
import com.jmadev.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {

    private final String LOG_TAG = ReviewActivityFragment.class.getSimpleName();
    public ReviewAdapter reviewAdapter;
    private RecyclerView rv;
    int movieId;

    public ReviewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        ArrayList<Review> reviewList = new ArrayList<>();

        movieId = getActivity().getIntent().getIntExtra("movieid", 0);
        rv = (RecyclerView) rootView.findViewById(R.id.rv_review);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        runFetchReviewsTask(reviewList);

        return rootView;
    }

    public void runFetchReviewsTask(List<Review> reviewList) {
        FetchReviewsTask reviewTask = new FetchReviewsTask(getActivity(),reviewAdapter, movieId, reviewList,  rv);
        reviewTask.execute();
    }


}
