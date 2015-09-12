package com.jmadev.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Cast {

    private String name;
    private String profile_path;

    public static final String NAME = "name";
    public static final String PROFILE_PATH = "profile_path";

    public void setName(JSONObject castJsonObject) throws JSONException {
        this.name = castJsonObject.getString(NAME);
    }

    public void setProfile_path(JSONObject castJonObject) throws JSONException {
        this.profile_path = "http://image.tmdb.org/t/p/w342" + castJonObject.getString(PROFILE_PATH);
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

}


