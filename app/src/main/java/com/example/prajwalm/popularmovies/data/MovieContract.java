package com.example.prajwalm.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by prajwalm on 22/08/17.
 */

public class MovieContract {


    private MovieContract(){

    }

    public static final String CONTENT_AUTHORITY = "com.example.prajwalm.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATHS_MOVIES = "movies";

    public static  final  class MovieEntry implements BaseColumns{


       public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATHS_MOVIES).build();

       public static final String TABLE_NAME="movies";

        public static final String MOVIE_NAME="name";

       public   static final String VOTE_AVERAGE="vote";

       public   static final String RELEASE_DATE="release";

        public static final String MOVIE_SUMMARY="summary";

        public static final String MOVIE_ID ="movie_id";

        public static final String MOVIE_IMAGE ="image";





    }







}
