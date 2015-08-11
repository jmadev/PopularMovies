package com.jmadev.popularmovies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MovieItemAdapter extends ArrayAdapter<Movie>  {
    private static final String LOG_TAG = MovieItemAdapter.class.getSimpleName();

    public MovieItemAdapter(Activity context, List<Movie> movies) {
        super (context, 0, movies);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        Log.v(LOG_TAG, "MovieItemAdapter Called!");
        Log.v(LOG_TAG, "Movie:: " + movie);
        final ImageView mImageView;
        final TextView textView;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.movie_image);
        textView = (TextView) convertView.findViewById(R.id.movie_title);
        textView.setText(movie.getOriginalTitle());
        String url = movie.getPosterPath();
        Log.v(LOG_TAG, "Movie poster url : " + url);

        Glide.with(getContext())
                .load(url)
                .fitCenter()
                .crossFade()
                .into(mImageView);

        return convertView;
    }
}
