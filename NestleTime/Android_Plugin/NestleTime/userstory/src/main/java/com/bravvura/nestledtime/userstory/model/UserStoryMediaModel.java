package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bravvura.nestledtime.mediagallery.model.MediaModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 28-02-2018.
 */

public class UserStoryMediaModel implements Parcelable {
    public String title;
    public ArrayList<MediaModel> mediaModels;
    public int mediaCount;

    public UserStoryMediaModel() {
        mediaModels = new ArrayList<>();
    }

    protected UserStoryMediaModel(Parcel in) {
        title = in.readString();
        mediaModels = in.createTypedArrayList(MediaModel.CREATOR);
    }

    public static final Creator<UserStoryMediaModel> CREATOR = new Creator<UserStoryMediaModel>() {
        @Override
        public UserStoryMediaModel createFromParcel(Parcel in) {
            return new UserStoryMediaModel(in);
        }

        @Override
        public UserStoryMediaModel[] newArray(int size) {
            return new UserStoryMediaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedList(mediaModels);
    }
}
