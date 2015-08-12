package com.jmadev.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String title;
    private double voteAverage;

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

    public Movie (Parcel in) {
        String[] sdata = new String[6];
        double[] ddata = new double[1];

        in.readStringArray(sdata);
        in.readDoubleArray(ddata);

        this.backdropPath = sdata[0];
        this.originalTitle = sdata[1];
        this.overview = sdata[2];
        this.releaseDate = sdata[3];
        this.posterPath = sdata[4];
        this.title = sdata[5];
        this.voteAverage = ddata[0];
    }

    public Movie( String backdropPath, String originalTitle, String overview, String releaseDate, String posterPath, String title, double voteAverage) {
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() { return backdropPath; }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return  releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return "Movie: \n" + backdropPath + "\n" + originalTitle + "\n" + overview + "\n" + releaseDate + "\n" + posterPath +
                "\n" + title + "\n" + voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
            this.backdropPath,
            this.originalTitle,
            this.overview,
            this.releaseDate,
            this.posterPath,
            this.title
        });

        dest.writeDoubleArray(new double[] {
                this.voteAverage
        });
    }
}
