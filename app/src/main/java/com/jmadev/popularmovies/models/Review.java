package com.jmadev.popularmovies.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Review implements Parcelable {

    private String author;
    private String content;

    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";

    public Review() { }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public void setAuthor(JSONObject reviewJsonObject) throws JSONException {
        this.author = reviewJsonObject.getString(AUTHOR);
    }

    public void setContent(JSONObject reviewJsonObject) throws JSONException {
        this.content = reviewJsonObject.getString(CONTENT);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
