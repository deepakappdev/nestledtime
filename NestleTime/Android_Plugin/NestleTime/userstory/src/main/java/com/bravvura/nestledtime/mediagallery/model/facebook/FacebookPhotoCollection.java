package com.bravvura.nestledtime.mediagallery.model.facebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 19-03-2018.
 */

public class FacebookPhotoCollection implements Parcelable {
    public ArrayList<FacebookItemData> data;

    public FacebookPhotoCollection() {
    }

    protected FacebookPhotoCollection(Parcel in) {
        data = in.createTypedArrayList(FacebookItemData.CREATOR);
    }

    public static final Creator<FacebookPhotoCollection> CREATOR = new Creator<FacebookPhotoCollection>() {
        @Override
        public FacebookPhotoCollection createFromParcel(Parcel in) {
            return new FacebookPhotoCollection(in);
        }

        @Override
        public FacebookPhotoCollection[] newArray(int size) {
            return new FacebookPhotoCollection[size];
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
