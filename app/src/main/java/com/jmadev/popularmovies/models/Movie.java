package com.jmadev.popularmovies.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Movie implements Parcelable {

    private int id;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String title;
    private double voteAverage;
    public static final String RESULTS = "results";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String TITLE = "title";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String _ID = "id";
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_RELEASE_DATE = 4;
    public static final int COL_BACKDROP_PATH = 5;
    public static final int COL_VOTE_AVERAGE = 6;

    public void setId(JSONObject movieJsonObject) throws JSONException {
        this.id = movieJsonObject.getInt(_ID);
    }
    public void setId(Cursor cursor) {
        this.id = cursor.getInt(COL_ID);
    }

    public void setBackdropPath(JSONObject movieJsonObject) throws JSONException {
        this.backdropPath = "http://image.tmdb.org/t/p/w780" + movieJsonObject.getString(BACKDROP_PATH);
    }

    public void setBackdropPath(Cursor cursor) {
        this.backdropPath = cursor.getString(COL_BACKDROP_PATH);
    }

    public void setOverview(JSONObject movieJsonObject) throws JSONException {
        this.overview = movieJsonObject.getString(OVERVIEW);
    }

    public void setOverview(Cursor cursor) {
        this.overview = cursor.getString(COL_OVERVIEW);
    }

    public void setReleaseDate(JSONObject movieJsonObject) throws JSONException {
        this.releaseDate = movieJsonObject.getString(RELEASE_DATE);
    }

    public void setReleaseDate(Cursor cursor) {
        this.releaseDate = cursor.getString(COL_RELEASE_DATE);
    }

    public void setPosterPath(JSONObject movieJsonObject) throws JSONException {
        this.posterPath = "http://image.tmdb.org/t/p/w780" + movieJsonObject.getString(POSTER_PATH);
    }

    public void setPosterPath(Cursor cursor) {
        this.posterPath = cursor.getString(COL_POSTER_PATH);
    }

    public void setTitle(JSONObject movieJsonObject) throws JSONException {
        this.title = movieJsonObject.getString(TITLE);
    }

    public void setTitle(Cursor cursor) {
        this.title = cursor.getString(COL_TITLE);
    }

    public void setVoteAverage(JSONObject movieJsonObject) throws JSONException {
        this.voteAverage = movieJsonObject.getDouble(VOTE_AVERAGE);
    }

    public void setVoteAverage(Cursor cursor) {
        this.voteAverage = cursor.getDouble(COL_VOTE_AVERAGE);
    }

    public Movie() {

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
        this.id = in.readInt();
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }


    public int getId() { return id; }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return "Movie: \n" + backdropPath + "\n" + overview + "\n" + releaseDate + "\n" + posterPath +
                "\n" + title + "\n" + voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
    }

}