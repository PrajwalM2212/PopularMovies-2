package com.example.prajwalm.popularmovies.Favourites;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prajwalm.popularmovies.R;
import com.example.prajwalm.popularmovies.data.MovieContract;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by prajwalm on 22/08/17.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {


   private Context context;
   private Cursor cursor;


    FavouritesAdapter(Context context , Cursor cursor){

       this.context =context;
        this.cursor=cursor;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item,parent,false);
       return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        TextView title =holder.favTitle;
        TextView summary = holder.favSummary;
        TextView releaseDate = holder.favReleaseDate;
        TextView voteAverage = holder.favVoteAverage;
        ImageView image = holder.favImage;

        String favTitle=null;
        String favSummary=null;
        String favReleaseDate=null;
        String favVoteAverage=null;
        String favImage=null;
        cursor.moveToPosition(position);
        {

           favTitle = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_NAME));
           favSummary =cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_SUMMARY));
           favReleaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE));
           favVoteAverage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.VOTE_AVERAGE));
           favImage=cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_IMAGE));
           holder.itemView.setTag(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));


        }
        title.setText(favTitle);
        summary.setText(favSummary);
        releaseDate.setText(favReleaseDate);
        voteAverage.setText(favVoteAverage);
        Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w342"+favImage).buildUpon();
        Uri uri =builder.build();
        Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(image);


    }


    public Cursor swapCursor(Cursor newCursor){

        if(cursor==newCursor){
            return null;
        }

        if(cursor!=null){
            cursor=newCursor;
        }

        if(newCursor!=null){
            this.notifyDataSetChanged();
        }


        return cursor;

    }


    @Override
    public int getItemCount() {
        if(cursor==null) {
            return 0;
        }else{
            return cursor.getCount();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       public TextView favTitle;
       public TextView favSummary;
       public TextView favReleaseDate;
       public TextView favVoteAverage;
       public ImageView favImage;

        public ViewHolder(View itemView) {
            super(itemView);

            favTitle = (TextView)itemView.findViewById(R.id.fav_title);
            favSummary=(TextView)itemView.findViewById(R.id.fav_summary);
            favVoteAverage=(TextView)itemView.findViewById(R.id.fav_voteAverage);
            favReleaseDate = (TextView)itemView.findViewById(R.id.fav_releaseDate);
            favImage =(ImageView)itemView.findViewById(R.id.fav_image);

        }
    }
}
