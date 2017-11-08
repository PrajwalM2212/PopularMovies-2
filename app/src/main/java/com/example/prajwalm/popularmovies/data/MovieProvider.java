package com.example.prajwalm.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by prajwalm on 22/08/17.
 */

public class MovieProvider extends ContentProvider {


    private static final int MOVIES_ID = 100;
    private static final int MOVIES_ITEM_ID =101;
    private static UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;



    public  static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATHS_MOVIES,MOVIES_ID);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATHS_MOVIES+"/#",MOVIES_ITEM_ID);


        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {


        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor retCursor=null;


        int match = sUriMatcher.match(uri);

        switch (match){

            case MOVIES_ID:

                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;


            case MOVIES_ITEM_ID:

                String id = uri.getLastPathSegment();
                //_id or _ID
                String mSelection = MovieContract.MovieEntry._ID+"=?";
                String[] mSelectionArgs = new String[]{id};


                retCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,projection,mSelection,mSelectionArgs,null,null,sortOrder);
                break;


            default:

                throw new UnsupportedOperationException("Unsupported");

        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {



        int match = sUriMatcher.match(uri);

        switch (match){


            case MOVIES_ID:

                return "vnd.android.cursor.dir"+"/"+MovieContract.CONTENT_AUTHORITY+"/"+MovieContract.PATHS_MOVIES;


            case MOVIES_ITEM_ID:

                return "vnd.android.cursor.item"+"/"+MovieContract.CONTENT_AUTHORITY+"/"+MovieContract.PATHS_MOVIES;


            default:

                throw new UnsupportedOperationException("Unsupported");



        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

         final SQLiteDatabase db = dbHelper.getWritableDatabase();

         long insertedId;

        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES_ID:

               insertedId= db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
               break;

            default:

                throw new UnsupportedOperationException("Unsupported");

        }




        Uri insertedUri = ContentUris.withAppendedId(uri,insertedId);

        getContext().getContentResolver().notifyChange(uri,null);
        return  insertedUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db =dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match){
            case MOVIES_ITEM_ID:
                String mSelection = MovieContract.MovieEntry._ID+"=?";
                String id = uri.getLastPathSegment();
                String[] mSelectionArgs = new String[]{id};


                rowsDeleted=db.delete(MovieContract.MovieEntry.TABLE_NAME,mSelection,mSelectionArgs);
                break;

            case MOVIES_ID:

                rowsDeleted=db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported");

        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
