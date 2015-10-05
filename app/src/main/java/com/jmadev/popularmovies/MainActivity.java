package com.jmadev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jmadev.popularmovies.models.Movie;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane;
    private static final String MOVIEDETAILFRAGMENT_TAG = "MDFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("mainactivity",
                        getIntent().getParcelableExtra("mainactivity"));

                MainActivityFragment maf = new MainActivityFragment();
                maf.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, new MovieDetailActivityFragment(), MOVIEDETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailActivityFragment.MOVIEDETAILACTIVITY, movie);

            MovieDetailActivityFragment mdaf = new MovieDetailActivityFragment();
            mdaf.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, mdaf, MOVIEDETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .putExtra(MovieDetailActivityFragment.MOVIEDETAILACTIVITY, movie);
            startActivity(intent);
        }

    }
}