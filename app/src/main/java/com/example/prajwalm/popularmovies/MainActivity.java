package com.example.prajwalm.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prajwalm.popularmovies.Favourites.FavouritesActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ClickListenerInterface, SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<ArrayList<JsonResults>> {

    //Enter your apiKey in the String API_VALUE below
    private ImageAdapter adapter;
    private RecyclerView recyclerView;
    public static final String API_KEY = "api_key";
    public static final String API_VALUE = "Enter your Api_Key here";
    private boolean topRated = false;
    private boolean popular = false;
    TextView networkView;
    TextView mEmptyView;
    ProgressBar mProgressBar;
    private int load_id = 0;
    private String search;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkView = (TextView) findViewById(R.id.network_text);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        run();


        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void run() {

        if (checkNetwork(this)) {
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<JsonResults>> loader = loaderManager.getLoader(load_id);
            if (loader == null) {
                loaderManager.initLoader(load_id, null, MainActivity.this);
            } else {
                loaderManager.restartLoader(load_id, null, this);
            }
        } else {

            networkView.setVisibility(View.VISIBLE);

        }


    }


    public int calculateColumnsCount(Context context) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 120;
        int count = (int) (dpWidth / scalingFactor);
        return count;

    }

    public String urlCreator(String string) {


        Uri.Builder builder = Uri.parse(string).buildUpon();
        builder.appendQueryParameter(API_KEY, API_VALUE);
        Uri uri = builder.build();
        return uri.toString();


    }

    public static boolean checkNetwork(Context context) {


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    @Override
    public void onItemClick(int position) {


        String pos = String.valueOf(position);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String typeSearch = sharedPreferences.getString(getString(R.string.key), getString(R.string.popularUrl));
        String Url = urlCreator(typeSearch);

        if (checkNetwork(this)) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, pos);
            intent.putExtra("Url", Url);
            startActivity(intent);


        } else {
            Toast.makeText(this, "No active Network", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.key))) {

            run();


        }


    }


    @Override
    public Loader<ArrayList<JsonResults>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<JsonResults>>(this) {


            @Override
            public void onStartLoading() {


                super.onStartLoading();
                mProgressBar.setVisibility(View.VISIBLE);
                networkView.setVisibility(View.INVISIBLE);

                forceLoad();


            }

            @Override
            public ArrayList<JsonResults> loadInBackground() {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String typeSearch = sharedPreferences.getString(getString(R.string.key), getString(R.string.popularUrl));
                search = urlCreator(typeSearch);

                ArrayList<JsonResults> titleResult = null;
                String json;
                try {

                    json = ImageGetter.httpUrlRequest(search);


                    titleResult = ImageGetter.JsonExtraction(json);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return titleResult;
            }


        };
    }

    ;


    @Override
    public void onLoadFinished(Loader<ArrayList<JsonResults>> loader, ArrayList<JsonResults> titleResult) {

        if (titleResult != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            layoutManager = new GridLayoutManager(MainActivity.this, calculateColumnsCount(this));
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ImageAdapter(MainActivity.this, titleResult, MainActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<JsonResults>> loader) {

    }


    public void onConfigurationChanged(Configuration configuration) {

        super.onConfigurationChanged(configuration);
    }


    public void onDestroy() {

        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;


    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.settings) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.fav) {

            Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);


    }


}