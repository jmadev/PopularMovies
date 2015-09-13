package com.jmadev.popularmovies.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.models.Cast;

import java.util.List;

public class TopCastAdapter extends ArrayAdapter<Cast> {

    private static final String LOG_TAG = TopCastAdapter.class.getSimpleName();

    public TopCastAdapter(Context context, List<Cast> cast) {
        super(context, 0, cast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Cast cast = getItem(position);

        final Button mButton;
        final ImageView mImageView;
        final TextView mTextView;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.top_cast_item, parent, false);

        }


        mImageView = (ImageView) convertView.findViewById(R.id.cast_image);
        mTextView = (TextView) convertView.findViewById(R.id.cast_name);
        mTextView.setText(cast.getName());
        String url = cast.getProfile_path();
        Log.v(LOG_TAG, url);
        Glide.with(getContext())
                .load(url)
                .fitCenter()
                .crossFade()
                .into(mImageView);


        return convertView;
    }

    public void setCast(List<Cast> listCast) {
        clear();
//        for(Cast cast : listCast){
//            add(cast);
//        }
        for (int i = 0; i < 5; i++) {
            add(listCast.get(i));
        }
    }
}
