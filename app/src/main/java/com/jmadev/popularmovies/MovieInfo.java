package com.jmadev.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jae on 8/1/2015.
 */
public class MovieInfo {

    public static final String RESULTS = "results";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String TITLE = "title";
    public static final String VOTE_AVERAGE = "vote_average";
    private final JSONObject jsonObject;

    public MovieInfo (String jsonObject) throws JSONException {
        this.jsonObject = new JSONObject(jsonObject);
    }

    public List<Movie> getMovies() throws JSONException {
        JSONArray movieArrayResults = jsonObject.getJSONArray(RESULTS);

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieArrayResults.length(); i++) {
            JSONObject movieJsonObject = movieArrayResults.getJSONObject(i);
            Movie movie = new Movie(getOriginalTitle(movieJsonObject), getOverview(movieJsonObject),
                    getReleaseDate(movieJsonObject), getPosterPath(movieJsonObject),
                    getTitle(movieJsonObject), getVoteAverage(movieJsonObject));
            movies.add(movie);
        }
        return movies;
    }

    public String getOriginalTitle(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(ORIGINAL_TITLE);
    }

    public String getOverview(JSONObject jsonObject) throws  JSONException {
        return jsonObject.getString(OVERVIEW);
    }

    public String getReleaseDate(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(RELEASE_DATE);
    }

    public String getPosterPath(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(POSTER_PATH);
    }

    public String getTitle(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(TITLE);
    }

    public double getVoteAverage(JSONObject jsonObject) throws JSONException {
        return jsonObject.getDouble(VOTE_AVERAGE);
    }






}
