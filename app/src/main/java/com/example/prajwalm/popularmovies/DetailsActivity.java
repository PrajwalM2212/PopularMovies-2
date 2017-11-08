package com.example.prajwalm.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prajwalm.popularmovies.data.MovieContract;
import com.example.prajwalm.popularmovies.trailers.TrailerRecycler;
import com.example.prajwalm.popularmovies.trailers.Trailers;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<DetailsMap>>,TrailerRecycler.ListItemClickListener{

    private int position;

    private TextView mTitleView;
    private TextView mOverView;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private ImageView mImageView;
    private int load_id =0;
    private String url;
    private Button mTrailerButton;
    private String id;
    private TextView mReviewText;
    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private String imagePath;
    private TrailerRecycler trailersAdapter;
    private RecyclerView trailerRecycler;
    private String  trailerString;
    private ArrayList<Trailers> trail;
    private ImageButton star;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }




        mTitleView = (TextView) findViewById(R.id.title);
        mUserRating = (TextView) findViewById(R.id.user_rating);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOverView = (TextView) findViewById(R.id.over_view);
        mImageView = (ImageView) findViewById(R.id.image);
        star =(ImageButton) findViewById(R.id.favourite);



        Intent in = getIntent();

        position = Integer.parseInt(in.getStringExtra(Intent.EXTRA_TEXT));
        url = in.getStringExtra("Url");

        if(MainActivity.checkNetwork(this)) {

           // new Sync().execute(url);
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<DetailsMap>> loader = loaderManager.getLoader(load_id);
            if(loader==null){
                loaderManager.initLoader(load_id,null,this);
            }else{
                loaderManager.restartLoader(load_id,null,this);
            }
        }else{

            star.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show();
        }




    }

    public void onConfigurationChanged(Configuration configuration){

        super.onConfigurationChanged(configuration);
    }


    @Override
    public Loader<ArrayList<DetailsMap>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<DetailsMap>>(this) {


            @Override
            public void onStartLoading(){
                super.onStartLoading();
                forceLoad();
            }
            @Override
            public ArrayList<DetailsMap> loadInBackground() {
                ArrayList<DetailsMap> details = null;
                String json;
                try {

                    json = ImageGetter.httpUrlRequest(url);


                    details = ImageGetter.JsonValues(json, position);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return details;
            }

            };
        }


    @Override
    public void onLoadFinished(Loader<ArrayList<DetailsMap>> loader, ArrayList<DetailsMap> details) {

        if (details != null) {
            DetailsMap detailsMap = details.get(0);
            mTitleView.setText(detailsMap.title);
            mOverView.setText(detailsMap.overView);
            mReleaseDate.setText(detailsMap.releaseDate);
            mUserRating.setText(detailsMap.userRating);
            id=detailsMap.id;
            imagePath=detailsMap.imageUrl;
            Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w342" + detailsMap.imageUrl).buildUpon();
            Uri uri = builder.build();
            // TextView textView = holder.textView;
            //textView.setText(uri.toString());
            Picasso.with(DetailsActivity.this).load(uri).into(mImageView);






            new ReviewTask().execute();
            trailerString=getTrailerString();
            new Task().execute(trailerString);




        }



    }

    @Override
    public void onLoaderReset(Loader<ArrayList<DetailsMap>> loader) {

    }

    @Override
    public void onItemClick(int position) {

        String key=null;

        Trailers objectTrailer;
        for(int i=0;i<trail.size();i++){
          objectTrailer = trail.get(position);
          key= objectTrailer.key;
        }
        String youTubeString = "https://www.youtube.com/watch?v=";
        String trail = youTubeString + key;


        Uri.Builder buildUri = Uri.parse(trail).buildUpon();
        Uri uri = buildUri.build();


        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }
    }





    private class ReviewTask extends AsyncTask<String,Void,ArrayList<Review>>{

        @Override
        protected ArrayList<Review> doInBackground(String... strings) {

            String BASE_URL_STRING ="http://api.themoviedb.org/3/movie/";
            String API_KEY ="/reviews?"+MainActivity.API_KEY+"="+MainActivity.API_VALUE;
            final String reviewUriString = BASE_URL_STRING+id+API_KEY;

            ArrayList<Review>  reviews =null;



            try{
                String jsonResponse = ImageGetter.httpUrlRequest(reviewUriString);
                reviews =ImageGetter.getReview(jsonResponse);
            }catch (IOException e){
                e.printStackTrace();
            }


            return reviews;

        }

        protected void onPostExecute(ArrayList<Review> reviews){


            if(reviews!=null){

                recyclerView = (RecyclerView)findViewById(R.id.reviewsRecyclerView);
                reviewsAdapter = new ReviewsAdapter(DetailsActivity.this,reviews);
                recyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
                recyclerView.setAdapter(reviewsAdapter);
                recyclerView.setHasFixedSize(true);



            }






        }
    }













    public class Task extends AsyncTask<String ,Void,ArrayList<Trailers>>{

        @Override
        protected ArrayList<Trailers> doInBackground(String... strings) {

              ArrayList<Trailers> trailers = null;

            try{
                String jsonResponse = ImageGetter.httpUrlRequest(strings[0]);
                trailers= ImageGetter.getTrailerKey(jsonResponse);
            }catch (IOException e){
                e.printStackTrace();
            }

            return trailers;

        }

        protected void onPostExecute(ArrayList<Trailers> trailers){







         trailerRecycler =(RecyclerView)findViewById(R.id.trailerRecycler);
         trailersAdapter = new TrailerRecycler(DetailsActivity.this,trailers,DetailsActivity.this);
         trailerRecycler.setAdapter(trailersAdapter);
         trailerRecycler.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
         trailerRecycler.setHasFixedSize(true);


            trail=trailers;



        }






    }



  private String getTrailerString(){


      String BASE_URL_STRING ="http://api.themoviedb.org/3/movie/";
      String API_KEY ="/videos?"+MainActivity.API_KEY+"="+MainActivity.API_VALUE;
      final String trailerUriString = BASE_URL_STRING+id+API_KEY;

      return  trailerUriString;
  }



    public void saveData(View view){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.MOVIE_NAME,mTitleView.getText().toString());
        contentValues.put(MovieContract.MovieEntry.MOVIE_SUMMARY,mOverView.getText().toString());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE,mReleaseDate.getText().toString());
        contentValues.put(MovieContract.MovieEntry.VOTE_AVERAGE,mUserRating.getText().toString());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID,id);
        contentValues.put(MovieContract.MovieEntry.MOVIE_IMAGE,imagePath);


       Uri insertedUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);

        String  id = insertedUri.getLastPathSegment();
        if(Integer.parseInt(id)==-1){
            Toast.makeText(this,"Already Saved",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show();
        }


    }



    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.share_menu,menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){


        int id= item.getItemId();

        if(MainActivity.checkNetwork(this)) {
            if (id == R.id.share) {

                String key=null;

                Trailers objectTrailer;
                for(int i=0;i<trail.size();i++){
                    objectTrailer = trail.get(0);
                    key= objectTrailer.key;
                }
                String youTubeString = "https://www.youtube.com/watch?v=";
                String trail = youTubeString + key;


                Uri.Builder buildUri = Uri.parse(trail).buildUpon();
                Uri uri = buildUri.build();


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,mTitleView.getText().toString()+"\n"+
                        mOverView.getText().toString()+"\n"+mUserRating.getText().toString()+"\n"+
                        mReleaseDate.getText().toString()+"\n"+uri

                );
                startActivity(Intent.createChooser(intent, "Movie details"));
                return true;

            }
        }

       return super.onOptionsItemSelected(item);




    }


}