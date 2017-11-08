package com.example.prajwalm.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by prajwalm on 21/08/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {


    private final ArrayList<Review> reviews;
    private final Context context;





    public ReviewsAdapter(Context context , ArrayList<Review> reviews){
        this.context =context;
        this.reviews =reviews;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        return new ViewHolder(viewItem);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Review review = reviews.get(position);

            TextView authorText = holder.authorText;
            TextView commentText = holder.commentText;

            authorText.setText(review.author);
            commentText.setText(review.comment);



        }






    @Override
    public int getItemCount() {

        return reviews.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView authorText;
        TextView commentText;

        public ViewHolder(View itemView) {
            super(itemView);
            authorText = (TextView)itemView.findViewById(R.id.author);
            commentText =(TextView)itemView.findViewById(R.id.comment);
        }





    }
}
