package com.example.prajwalm.popularmovies.trailers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prajwalm.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by prajwalm on 25/08/17.
 */

public class TrailerRecycler extends RecyclerView.Adapter<TrailerRecycler.ViewHolder>{

    private ArrayList<Trailers> trailers;
    private Context context;
    final private ListItemClickListener listItemClickListener;


    public TrailerRecycler(Context context , ArrayList<Trailers> trailers,ListItemClickListener listItemClickListener){


        this.trailers=trailers;
        this.context=context;
        this.listItemClickListener =listItemClickListener;

    }

    public interface ListItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Trailers trailerObject = trailers.get(position);

        TextView trailerName = holder.trailerName;
        holder.itemView.setTag(trailerObject.key);

        trailerName.setText(trailerObject.trailerName);


    }

    @Override
    public int getItemCount() {
        if(trailers==null){
            return 0;
        }

        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView trailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView)itemView.findViewById(R.id.trailerItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemClickListener.onItemClick(position);
        }
    }
}
