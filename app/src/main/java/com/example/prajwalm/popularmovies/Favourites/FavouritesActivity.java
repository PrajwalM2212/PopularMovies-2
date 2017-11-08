package com.example.prajwalm.popularmovies.Favourites;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prajwalm.popularmovies.R;
import com.example.prajwalm.popularmovies.data.MovieContract;

public class FavouritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private FavouritesAdapter favouritesAdapter;
    private RecyclerView recyclerView;
    private int cursorLoadId=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);



        recyclerView=(RecyclerView)findViewById(R.id.favRecyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int id = (int)viewHolder.itemView.getTag();
                String idString = Integer.toString(id);

                Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(idString).build();

                getContentResolver().delete(uri,null,null);

                getLoaderManager().restartLoader(cursorLoadId,null,FavouritesActivity.this);


            }
        }){

        }.attachToRecyclerView(recyclerView);


        LoaderManager loaderManager = getLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(cursorLoadId);
        if(loader==null) {
            loaderManager.initLoader(cursorLoadId, null, this);
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {

            @Override

            public void onStartLoading(){

                forceLoad();

            }

            @Override
            public Cursor loadInBackground() {
                try{
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);

                }catch (SQLException e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {



        favouritesAdapter = new FavouritesAdapter(this,cursor);
        recyclerView.setAdapter(favouritesAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL );
        recyclerView.addItemDecoration(dividerItemDecoration);




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    protected void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(cursorLoadId,null,this);
    }

    public void onConfigurationChanged(Configuration configuration){

        super.onConfigurationChanged(configuration);
    }


    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.fav_menu,menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){

       int id = item.getItemId();

        if(id==R.id.deleteAll) {
            getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
            getLoaderManager().restartLoader(cursorLoadId,null,FavouritesActivity.this);
            return true;

        }

      return super.onOptionsItemSelected(item);


    }

}
