package com.example.spearmint;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Base class for the Post object with fields of type String
 * has getter methods so other classes can access the information held by a Post
 * Has fields "experiment title", "text"
 * @author Daniel
 */

public class Post implements Parcelable {

    private String postOwner;
    private String postText;

    Post(String postOwner, String postText){
        this.postOwner = postOwner;
        this.postText = postText;
    }

    protected Post(Parcel in) {
        postOwner = in.readString();
        postText = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPostOwner() {
        return this.postOwner;
    }

    public String getPostText() {
        return this.postText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postOwner);
        dest.writeString(postText);
    }


}
