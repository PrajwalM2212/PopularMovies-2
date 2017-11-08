package com.example.prajwalm.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prajwalm on 22/08/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="movies.db";

    private static final int DATABASE_VERSION =1;





    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME +"(" + MovieContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT ," + MovieContract.MovieEntry.MOVIE_NAME +" TEXT UNIQUE NOT NULL ," + MovieContract.MovieEntry.MOVIE_SUMMARY+
                " TEXT NOT NULL ,"+ MovieContract.MovieEntry.RELEASE_DATE +" TEXT NOT NULL ,"+ MovieContract.MovieEntry.MOVIE_ID +" TEXT NOT NULL ," + MovieContract.MovieEntry.MOVIE_IMAGE+" TEXT NOT NULL ,"+MovieContract.MovieEntry.VOTE_AVERAGE+
                " TEXT NOT NULL );" ;





        sqLiteDatabase.execSQL(CREATE_TABLE);





    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
