package com.jmadev.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    List<Review> reviews;
    Context mContext;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewViewHolder rvh = new ReviewViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.reviewAuthor.setText(reviews.get(position).getAuthor());
        holder.reviewContent.setText(reviews.get(position).getContent());
        Log.v(LOG_TAG, reviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


}

