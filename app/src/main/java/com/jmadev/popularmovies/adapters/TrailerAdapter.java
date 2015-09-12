package com.jmadev.popularmovies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.models.Trailer;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super (context, 0, trailers);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);
        Log.v(LOG_TAG, "TrailerAdapter Called!");
        Log.v(LOG_TAG, "Trailer: " + trailer);

        final ImageView mImageView;
        final TextView textView;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trailer_item, parent, false);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.trailer_image);
        textView = (TextView) convertView.findViewById(R.id.trailer_name);
        textView.setText(trailer.getName());
        String url = "http://img.youtube.com/vi/" + trailer.getSource() + "/0.jpg";
        Log.v(LOG_TAG, "Movie Trailer url : " + url);
        Glide.with(getContext())
                .load(url)
                .fitCenter()
                .crossFade()
                .into(mImageView);

        return convertView;
    }

    public void setTrailers(List<Trailer> listTrailers) {
        clear();
        for(Trailer trailer : listTrailers) {
            add(trailer);
        }
    }
}
