package com.jmadev.popularmovies;

/**
 * Created by Jae on 8/1/2015.
 */
public class Movie {
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String title;
    private double voteAverage;

    public Movie( String originalTitle, String overview, String releaseDate, String posterPath, String title, double voteAverage) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

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
}
