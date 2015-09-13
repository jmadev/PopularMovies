package com.jmadev.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmadev.popularmovies.adapters.AllCastAdapter;
import com.jmadev.popularmovies.models.Cast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllCastActivityFragment extends Fragment {
    private final String LOG_TAG = AllCastActivityFragment.class.getSimpleName();
    private AllCastAdapter allCastAdapter;
    private List<Cast> mListCast;
    private RecyclerView rv;
    public AllCastActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_cast, container, false);

        ArrayList<Cast> castList = getActivity().getIntent().getParcelableArrayListExtra("castList");

        rv = (RecyclerView) rootView.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData(castList);
        initializeAdapter(castList);

        return rootView;
    }



    private void initializeData(List<Cast> castList) {
        mListCast = new ArrayList<>();
        for (int i = 0; i < castList.size(); i++) {
            mListCast.add(castList.get(i));
            Log.v(LOG_TAG, mListCast.get(i).getName());
        }
    }

    private void initializeAdapter(ArrayList<Cast> castList) {
        allCastAdapter = new AllCastAdapter(getActivity(), castList);
        rv.setAdapter(allCastAdapter);
    }
}
