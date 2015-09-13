package com.jmadev.popularmovies.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmadev.popularmovies.R;
import com.jmadev.popularmovies.models.Cast;

import java.util.List;

public class AllCastAdapter extends RecyclerView.Adapter<AllCastAdapter.CastViewHolder> {

    private static final String LOG_TAG = TopCastAdapter.class.getSimpleName();

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView castPhoto;
        TextView castName;
        TextView castCharacter;


        CastViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.all_cast_cv);
            castPhoto = (ImageView) itemView.findViewById(R.id.cast_image);
            castName = (TextView) itemView.findViewById(R.id.cast_name);
            castCharacter = (TextView) itemView.findViewById(R.id.cast_character);
        }
    }

    List<Cast> cast;
    Context mContext;

    public AllCastAdapter(Context context, List<Cast> cast) {
        this.mContext = context;
        this.cast = cast;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_cast_item, parent, false);
        CastViewHolder cvh = new CastViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        holder.castName.setText(cast.get(position).getName());
        holder.castCharacter.setText(cast.get(position).getCharacter());
        String url = cast.get(position).getProfile_path();
        Log.v(LOG_TAG, url);
        Glide.with(mContext)
                .load(url)
                .fitCenter()
                .crossFade()
                .into(holder.castPhoto);


    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

}
