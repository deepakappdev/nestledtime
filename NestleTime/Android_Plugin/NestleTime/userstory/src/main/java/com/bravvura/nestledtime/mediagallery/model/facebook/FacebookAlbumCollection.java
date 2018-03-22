package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookAlbumCollection implements Parcelable {
    public ArrayList<FacebookItemData> data;

    public FacebookAlbumCollection() {
    }

    protected FacebookAlbumCollection(Parcel in) {
        data = in.createTypedArrayList(FacebookItemData.CREATOR);
    }

    public static final Creator<FacebookAlbumCollection> CREATOR = new Creator<FacebookAlbumCollection>() {
        @Override
        public FacebookAlbumCollection createFromParcel(Parcel in) {
            return new FacebookAlbumCollection(in);
        }

        @Override
        public FacebookAlbumCollection[] newArray(int size) {
            return new FacebookAlbumCollection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}
