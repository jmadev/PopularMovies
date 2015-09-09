package com.jmadev.popularmovies;


import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {

    private String name;
    private String source;

    public static final String NAME = "name";
    public static final String SOURCE = "source";



    public void setName(JSONObject trailerJsonObject) throws JSONException {
        this.name = trailerJsonObject.getString(NAME);
    }

    public void setSource(JSONObject trailerJsonObject) throws JSONException {
        this.source = trailerJsonObject.getString(SOURCE);
    }



    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }







}
