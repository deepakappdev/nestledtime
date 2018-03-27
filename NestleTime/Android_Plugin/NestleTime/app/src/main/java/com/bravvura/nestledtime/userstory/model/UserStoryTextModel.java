package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 28-02-2018.
 */

public class UserStoryTextModel implements Parcelable {
    public String data;
    public boolean autoFocus;
    public boolean isEdited;

    public UserStoryTextModel() {
    }

    protected UserStoryTextModel(Parcel in) {
        data = in.readString();
    }

    public static final Creator<UserStoryTextModel> CREATOR = new Creator<UserStoryTextModel>() {
        @Override
        public UserStoryTextModel createFromParcel(Parcel in) {
            return new UserStoryTextModel(in);
        }

        @Override
        public UserStoryTextModel[] newArray(int size) {
            return new UserStoryTextModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }
}
