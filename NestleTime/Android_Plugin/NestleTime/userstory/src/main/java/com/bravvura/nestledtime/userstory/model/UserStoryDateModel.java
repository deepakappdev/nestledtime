package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 28-02-2018.
 */

public class UserStoryDateModel implements Parcelable{
    public Date date;

    public UserStoryDateModel(){}
    protected UserStoryDateModel(Parcel in) {
        date = new Date(in.readLong());
    }

    public static final Creator<UserStoryDateModel> CREATOR = new Creator<UserStoryDateModel>() {
        @Override
        public UserStoryDateModel createFromParcel(Parcel in) {
            return new UserStoryDateModel(in);
        }

        @Override
        public UserStoryDateModel[] newArray(int size) {
            return new UserStoryDateModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
    }
}
