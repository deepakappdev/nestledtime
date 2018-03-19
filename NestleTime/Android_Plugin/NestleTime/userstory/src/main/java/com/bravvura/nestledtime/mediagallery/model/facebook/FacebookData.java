package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookData implements Parcelable {
    public ArrayList<FacebookAlbumData> data;

    public FacebookData() {
    }

    protected FacebookData(Parcel in) {
        data = in.createTypedArrayList(FacebookAlbumData.CREATOR);
    }

    public static final Creator<FacebookData> CREATOR = new Creator<FacebookData>() {
        @Override
        public FacebookData createFromParcel(Parcel in) {
            return new FacebookData(in);
        }

        @Override
        public FacebookData[] newArray(int size) {
            return new FacebookData[size];
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
