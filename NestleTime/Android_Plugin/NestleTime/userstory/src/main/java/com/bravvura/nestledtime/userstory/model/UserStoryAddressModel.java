package com.bravvura.nestledtime.userstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 28-02-2018.
 */

public class UserStoryAddressModel implements Parcelable{
    public LatLng latLng;
    public String placeName;
    public String placeAddress;

    public UserStoryAddressModel(){}
    protected UserStoryAddressModel(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        placeName = in.readString();
        placeAddress = in.readString();
    }

    public static final Creator<UserStoryAddressModel> CREATOR = new Creator<UserStoryAddressModel>() {
        @Override
        public UserStoryAddressModel createFromParcel(Parcel in) {
            return new UserStoryAddressModel(in);
        }

        @Override
        public UserStoryAddressModel[] newArray(int size) {
            return new UserStoryAddressModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(latLng, flags);
        dest.writeString(placeName);
        dest.writeString(placeAddress);
    }
}
