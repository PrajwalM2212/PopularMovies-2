package com.example.prajwalm.popularmovies;

import android.net.Uri;
import android.util.Log;

import com.example.prajwalm.popularmovies.trailers.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


class ImageGetter {
    private static final String LOG_TAG = ImageGetter.class.getSimpleName();


    private ImageGetter() {

    }


     static ArrayList<JsonResults> JsonExtraction(String jsonResponse) {


        ArrayList<JsonResults> jsonResultsArrayList = new ArrayList<>();


        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {

                JsonResults jsonResults = new JsonResults();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                jsonResults.title = jsonObject1.getString("title");
                jsonResults.bitmapUrl = jsonObject1.getString("poster_path");
                jsonResults.id = jsonObject1.getString("id");
                jsonResults.backgroundPath=jsonObject1.getString("backdrop_path");
                jsonResultsArrayList.add(jsonResults);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResultsArrayList;

/*
 Uri.Builder builder = Uri.parse("http://image.tmdb.org/t/p/w185/").buildUpon();
 builder.appendPath(bitmapString);
 builder.build();
 URL bitmapUrl;

 //bitmapUrl =new URL(builder.toString());**/


    }


    private static URL createUrl(String UrlString) {


        Uri.Builder builder = Uri.parse(UrlString).buildUpon();
        builder.build();

        URL url = null;

        try {

            url = new URL(builder.toString());

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedException");
        }

        return url;
    }



     static String httpUrlRequest(String query) throws IOException {

        String jsonResponse = "";
        URL url = createUrl(query);


        if (url == null) {
            return jsonResponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }


        return jsonResponse;

    }


    private static String readFromStream(InputStream inputStream) throws IOException {


        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        String line = bufferedReader.readLine();

        while (line != null) {
            output.append(line);
            line = bufferedReader.readLine();
        }

        return output.toString();


    }


     static ArrayList<DetailsMap> JsonValues(String jsonResponse, int position) {


        //ArrayList<String> list = new ArrayList<>();
        ArrayList<DetailsMap> details=new ArrayList<>();
        DetailsMap detailsMap = new DetailsMap();



        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("results");


            for(int i=0 ;i<jsonArray.length();i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                detailsMap.title = jsonObject1.getString("original_title");
                detailsMap.overView =jsonObject1.getString("overview");
                detailsMap.releaseDate =jsonObject1.getString("release_date");
                detailsMap.userRating =jsonObject1.getString("vote_average");
                detailsMap.imageUrl=jsonObject1.getString("poster_path");
                detailsMap.id=jsonObject1.getString("id");
                detailsMap.backgroundPath=jsonObject1.getString("backdrop_path");
                details.add(detailsMap);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return details;


    }


    static ArrayList<Trailers> getTrailerKey(String jsonResponse){

        ArrayList<Trailers> trailerData = new ArrayList<>();

        try{

            JSONObject trailerObject = new JSONObject(jsonResponse);
            JSONArray  trailerArray = trailerObject.getJSONArray("results");

            for(int i=0;i<trailerArray.length();i++){
                Trailers trailers = new Trailers();
                JSONObject trailerKey1 = trailerArray.getJSONObject(i);
                trailers.key= trailerKey1.getString("key");
                trailers.trailerName =trailerKey1.getString("name");
                trailerData.add(trailers);

            }



        }catch (JSONException e){
            e.printStackTrace();
        }



        return trailerData;


    }


    static ArrayList<Review> getReview(String jsonResponse){


        ArrayList<Review> reviews = new ArrayList<>();


        try{
            JSONObject reviewObject = new JSONObject(jsonResponse);
            JSONArray  reviewArray = reviewObject.getJSONArray("results");


            for(int i=0;i<reviewArray.length();i++){
                Review review = new Review();
                JSONObject ob = reviewArray.getJSONObject(i);
                review.author = ob.getString("author");
                review.comment = ob.getString("content");

                reviews.add(review);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }




        return reviews;



    }



}






