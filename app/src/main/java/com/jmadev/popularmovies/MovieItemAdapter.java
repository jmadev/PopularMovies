package com.jmadev.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MovieItemAdapter extends ArrayAdapter<MovieItem> {
    //private static final String LOG_TAG = MovieItemAdapter.class.getSimpleName();

    public MovieItemAdapter(Activity context, List<MovieItem> movieItems) {
        super (context, 0, movieItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieItem movieItem = getItem(position);
        final ImageView mImageView;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.movie_image);

        String url = movieItem.img_url;

        Glide.with(getContext())
                .load(url)
                //.centerCrop()
                .fitCenter()
                .crossFade()
                .into(mImageView);

        return convertView;
    }
}
