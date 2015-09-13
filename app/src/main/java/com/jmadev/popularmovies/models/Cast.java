package com.jmadev.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Cast implements Parcelable {

    private String name;
    private String character;
    private String profile_path;

    public static final String NAME = "name";
    public static final String CHARACTER = "character";
    public static final String PROFILE_PATH = "profile_path";

    public Cast() { }

    public static final Parcelable.Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public Cast(Parcel in) {
        this.name = in.readString();
        this.character = in.readString();
        this.profile_path = in.readString();
    }





    public void setName(JSONObject castJsonObject) throws JSONException {
        this.name = castJsonObject.getString(NAME);
    }

    public void setCharacter(JSONObject castJsonObject) throws  JSONException {
        this.character = castJsonObject.getString(CHARACTER);
    }

    public void setProfile_path(JSONObject castJonObject) throws JSONException {
        this.profile_path = "http://image.tmdb.org/t/p/w342" + castJonObject.getString(PROFILE_PATH);
    }

    public String getName() {
        return name;
    }

    public String getCharacter() { return character; }

    public String getProfile_path() {
        return profile_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(character);
        dest.writeString(profile_path);
    }
}


